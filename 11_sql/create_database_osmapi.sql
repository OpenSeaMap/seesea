DROP DATABASE IF EXISTS osmapi;

CREATE DATABASE osmapi
    WITH 
    OWNER = osm
    ENCODING = 'UTF8'
    LC_COLLATE = 'en_US.UTF-8'
    LC_CTYPE = 'en_US.UTF-8'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

-- Create the PostGIS extension if it doesn't exist
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_extension
        WHERE extname = 'postgis'
    ) THEN
        CREATE EXTENSION postgis;
    END IF;
END $$;