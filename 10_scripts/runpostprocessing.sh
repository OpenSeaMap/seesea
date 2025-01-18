#!/usr/bin/env bash
set -e
trap 'rm -f ~/postproc.lock' EXIT

LOCKFILE=./postproc.lock
LOGFILE=/app/logs/psql.log

run_psql_command() {
    local db=$1
    local query=$2

    # Log the query execution
    echo "Executing query on database '$db': $query" | tee -a "$LOGFILE"

    # Execute psql command and redirect stdout and stderr to console and log file
    if ! psql -h postgis -p 5432 -U postgres "$db" -c "$query" >> "$LOGFILE" 2>&1; then
        echo "Failed to execute query on $db: $query" | tee -a "$LOGFILE" >&2
        rm -f "$LOCKFILE"
        exit 1
    fi
}

if [ ! -f "$LOCKFILE" ]; then

    cd /app/postprocess/

    touch "$LOCKFILE"
    echo "Starting script..." | tee -a "$LOGFILE"

    # Pull data from OSM API
    run_psql_command "depth" "select osmapi_tables.pullfromosmapi()"

    # Run the Eclipse post-processing
    if ! ./eclipse; then
        echo "Failed to execute ./eclipse!" | tee -a "$LOGFILE" >&2
        rm -f "$LOCKFILE"
        exit 1
    fi

    # Run database processes
    run_psql_command "depth" "select dofillrawrendertables()"
    run_psql_command "depth" "select domergerun()"
    run_psql_command "osmapi" "select pullfromdepth()"

    echo "Script completed successfully." | tee -a "$LOGFILE"
else
    echo "Script is already running. Exiting." | tee -a "$LOGFILE"
fi

rm -f "$LOCKFILE"