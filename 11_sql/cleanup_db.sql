-- Terminate all active connections to the depth database
SELECT pg_terminate_backend(pg_stat_activity.pid)
FROM pg_stat_activity
WHERE pg_stat_activity.datname = 'depth'
  AND pid <> pg_backend_pid();

-- Drop the depth database
DROP DATABASE IF EXISTS depth;

-- Terminate all active connections to the osmapi database
SELECT pg_terminate_backend(pg_stat_activity.pid)
FROM pg_stat_activity
WHERE pg_stat_activity.datname = 'osmapi'
  AND pid <> pg_backend_pid();

-- Drop the osmapi database
DROP DATABASE IF EXISTS osmapi;


DO $$ 
BEGIN
    IF EXISTS (
        SELECT 1
        FROM pg_roles
        WHERE rolname = 'osm'
    ) THEN
        EXECUTE 'REVOKE ALL PRIVILEGES ON SCHEMA public FROM osm';
    END IF;
END $$;

-- Drop the osm user/role
DROP ROLE IF EXISTS osm;

-- Drop the osmapi user/role
DROP ROLE IF EXISTS osmapi;
