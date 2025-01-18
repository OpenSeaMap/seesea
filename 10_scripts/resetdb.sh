#!/bin/bash

SCRIPT_PATH="/seesea/11_sql/"

psql -h postgis -U postgres -f ${SCRIPT_PATH}/cleanup_db.sql
psql -h postgis -U postgres -f ${SCRIPT_PATH}/create_database_user.sql
psql -h postgis -U postgres -f ${SCRIPT_PATH}/create_database_depth.sql
psql -h postgis -U postgres -f ${SCRIPT_PATH}/create_database_osmapi.sql
psql -h postgis -U postgres -d osmapi -f ${SCRIPT_PATH}/osmapi_schema.sql
psql -h postgis -U postgres -d depth -f  ${SCRIPT_PATH}/depth_schema.sql

psql -h postgis -U postgres -d osmapi -f ${SCRIPT_PATH}/testdata.sql

psql -h postgis -U postgres -d osmapi -c "GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO osm;"
psql -h postgis -U postgres -d osmapi -c "GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA depth_tables TO osm;"
psql -h postgis -U postgres -d osmapi -c "GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO osm;"
psql -h postgis -U postgres -d osmapi -c "GRANT ALL PRIVILEGES ON ALL FUNCTIONS IN SCHEMA public TO osm;"
psql -h postgis -U postgres -d osmapi -c "CREATE USER MAPPING FOR postgres SERVER depth OPTIONS (user 'osm', password '!2osm2!');"

psql -h postgis -U postgres -d depth  -c "GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO osm;"
psql -h postgis -U postgres -d depth  -c "GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA osmapi_tables TO osm;"
psql -h postgis -U postgres -d depth  -c "GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO osm;"
psql -h postgis -U postgres -d depth  -c "GRANT ALL PRIVILEGES ON ALL FUNCTIONS IN SCHEMA public TO osm;"
psql -h postgis -U postgres -d depth  -c "CREATE USER MAPPING FOR postgres SERVER osmapi OPTIONS (user 'osm', password '!2osm2!');"

psql -h postgis -U postgres -d osmapi -c "CREATE EXTENSION pldbgapi;"
psql -h postgis -U postgres -d depth  -c "CREATE EXTENSION pldbgapi;"

psql -h postgis -U postgres   -c "\d"