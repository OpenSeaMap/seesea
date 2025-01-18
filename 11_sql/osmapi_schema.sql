--
-- PostgreSQL database dump
--

-- Dumped from database version 9.6.24
-- Dumped by pg_dump version 9.6.24

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: depth_fdw; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA depth_fdw;


ALTER SCHEMA depth_fdw OWNER TO postgres;

--
-- Name: depth_tables; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA depth_tables;


ALTER SCHEMA depth_tables OWNER TO postgres;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


--
-- Name: postgis; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS postgis WITH SCHEMA public;


--
-- Name: EXTENSION postgis; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION postgis IS 'PostGIS geometry, geography, and raster spatial types and functions';


--
-- Name: postgres_fdw; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS postgres_fdw WITH SCHEMA public;


--
-- Name: EXTENSION postgres_fdw; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION postgres_fdw IS 'foreign-data wrapper for remote PostgreSQL servers';


--
-- Name: addbbox(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.addbbox(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_addBBOX';


ALTER FUNCTION public.addbbox(public.geometry) OWNER TO postgres;

--
-- Name: addpoint(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.addpoint(public.geometry, public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_addpoint';


ALTER FUNCTION public.addpoint(public.geometry, public.geometry) OWNER TO postgres;

--
-- Name: addpoint(public.geometry, public.geometry, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.addpoint(public.geometry, public.geometry, integer) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_addpoint';


ALTER FUNCTION public.addpoint(public.geometry, public.geometry, integer) OWNER TO postgres;

--
-- Name: adjustsequence(character varying, bigint); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.adjustsequence(cname character varying, ivalue bigint) RETURNS void
    LANGUAGE plpgsql
    AS $$
declare
	iSeqVal bigint;
begin
	if iValue is null then
		return;
	end if;
	execute 'select last_value from ' || cName into iSeqVal;
	while iSeqVal < iValue 
	loop
		iSeqVal := nextval( cName );
	end loop;
end;
$$;


ALTER FUNCTION public.adjustsequence(cname character varying, ivalue bigint) OWNER TO postgres;

--
-- Name: affine(public.geometry, double precision, double precision, double precision, double precision, double precision, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.affine(public.geometry, double precision, double precision, double precision, double precision, double precision, double precision) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT st_affine($1,  $2, $3, 0,  $4, $5, 0,  0, 0, 1,  $6, $7, 0)$_$;


ALTER FUNCTION public.affine(public.geometry, double precision, double precision, double precision, double precision, double precision, double precision) OWNER TO postgres;

--
-- Name: affine(public.geometry, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.affine(public.geometry, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_affine';


ALTER FUNCTION public.affine(public.geometry, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision) OWNER TO postgres;

--
-- Name: area(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.area(public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_area_polygon';


ALTER FUNCTION public.area(public.geometry) OWNER TO postgres;

--
-- Name: area2d(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.area2d(public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_area_polygon';


ALTER FUNCTION public.area2d(public.geometry) OWNER TO postgres;

--
-- Name: asbinary(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.asbinary(public.geometry) RETURNS bytea
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_asBinary';


ALTER FUNCTION public.asbinary(public.geometry) OWNER TO postgres;

--
-- Name: asbinary(public.geometry, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.asbinary(public.geometry, text) RETURNS bytea
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_asBinary';


ALTER FUNCTION public.asbinary(public.geometry, text) OWNER TO postgres;

--
-- Name: asewkb(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.asewkb(public.geometry) RETURNS bytea
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'WKBFromLWGEOM';


ALTER FUNCTION public.asewkb(public.geometry) OWNER TO postgres;

--
-- Name: asewkb(public.geometry, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.asewkb(public.geometry, text) RETURNS bytea
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'WKBFromLWGEOM';


ALTER FUNCTION public.asewkb(public.geometry, text) OWNER TO postgres;

--
-- Name: asewkt(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.asewkt(public.geometry) RETURNS text
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_asEWKT';


ALTER FUNCTION public.asewkt(public.geometry) OWNER TO postgres;

--
-- Name: asgml(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.asgml(public.geometry) RETURNS text
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT _ST_AsGML(2, $1, 15, 0, null, null)$_$;


ALTER FUNCTION public.asgml(public.geometry) OWNER TO postgres;

--
-- Name: asgml(public.geometry, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.asgml(public.geometry, integer) RETURNS text
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT _ST_AsGML(2, $1, $2, 0, null, null)$_$;


ALTER FUNCTION public.asgml(public.geometry, integer) OWNER TO postgres;

--
-- Name: ashexewkb(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.ashexewkb(public.geometry) RETURNS text
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_asHEXEWKB';


ALTER FUNCTION public.ashexewkb(public.geometry) OWNER TO postgres;

--
-- Name: ashexewkb(public.geometry, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.ashexewkb(public.geometry, text) RETURNS text
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_asHEXEWKB';


ALTER FUNCTION public.ashexewkb(public.geometry, text) OWNER TO postgres;

--
-- Name: askml(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.askml(public.geometry) RETURNS text
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT _ST_AsKML(2, ST_Transform($1,4326), 15, null)$_$;


ALTER FUNCTION public.askml(public.geometry) OWNER TO postgres;

--
-- Name: askml(public.geometry, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.askml(public.geometry, integer) RETURNS text
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT _ST_AsKML(2, ST_transform($1,4326), $2, null)$_$;


ALTER FUNCTION public.askml(public.geometry, integer) OWNER TO postgres;

--
-- Name: askml(integer, public.geometry, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.askml(integer, public.geometry, integer) RETURNS text
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT _ST_AsKML($1, ST_Transform($2,4326), $3, null)$_$;


ALTER FUNCTION public.askml(integer, public.geometry, integer) OWNER TO postgres;

--
-- Name: assvg(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.assvg(public.geometry) RETURNS text
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_asSVG';


ALTER FUNCTION public.assvg(public.geometry) OWNER TO postgres;

--
-- Name: assvg(public.geometry, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.assvg(public.geometry, integer) RETURNS text
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_asSVG';


ALTER FUNCTION public.assvg(public.geometry, integer) OWNER TO postgres;

--
-- Name: assvg(public.geometry, integer, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.assvg(public.geometry, integer, integer) RETURNS text
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_asSVG';


ALTER FUNCTION public.assvg(public.geometry, integer, integer) OWNER TO postgres;

--
-- Name: astext(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.astext(public.geometry) RETURNS text
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_asText';


ALTER FUNCTION public.astext(public.geometry) OWNER TO postgres;

--
-- Name: azimuth(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.azimuth(public.geometry, public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_azimuth';


ALTER FUNCTION public.azimuth(public.geometry, public.geometry) OWNER TO postgres;

--
-- Name: bdmpolyfromtext(text, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.bdmpolyfromtext(text, integer) RETURNS public.geometry
    LANGUAGE plpgsql IMMUTABLE STRICT
    AS $_$
DECLARE
	geomtext alias for $1;
	srid alias for $2;
	mline geometry;
	geom geometry;
BEGIN
	mline := ST_MultiLineStringFromText(geomtext, srid);

	IF mline IS NULL
	THEN
		RAISE EXCEPTION 'Input is not a MultiLinestring';
	END IF;

	geom := ST_Multi(ST_BuildArea(mline));

	RETURN geom;
END;
$_$;


ALTER FUNCTION public.bdmpolyfromtext(text, integer) OWNER TO postgres;

--
-- Name: bdpolyfromtext(text, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.bdpolyfromtext(text, integer) RETURNS public.geometry
    LANGUAGE plpgsql IMMUTABLE STRICT
    AS $_$
DECLARE
	geomtext alias for $1;
	srid alias for $2;
	mline geometry;
	geom geometry;
BEGIN
	mline := ST_MultiLineStringFromText(geomtext, srid);

	IF mline IS NULL
	THEN
		RAISE EXCEPTION 'Input is not a MultiLinestring';
	END IF;

	geom := ST_BuildArea(mline);

	IF GeometryType(geom) != 'POLYGON'
	THEN
		RAISE EXCEPTION 'Input returns more then a single polygon, try using BdMPolyFromText instead';
	END IF;

	RETURN geom;
END;
$_$;


ALTER FUNCTION public.bdpolyfromtext(text, integer) OWNER TO postgres;

--
-- Name: boundary(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.boundary(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'boundary';


ALTER FUNCTION public.boundary(public.geometry) OWNER TO postgres;

--
-- Name: buffer(public.geometry, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.buffer(public.geometry, double precision) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT COST 100
    AS '$libdir/postgis-2.3', 'buffer';


ALTER FUNCTION public.buffer(public.geometry, double precision) OWNER TO postgres;

--
-- Name: buffer(public.geometry, double precision, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.buffer(public.geometry, double precision, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT ST_Buffer($1, $2, $3)$_$;


ALTER FUNCTION public.buffer(public.geometry, double precision, integer) OWNER TO postgres;

--
-- Name: buildarea(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.buildarea(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT COST 100
    AS '$libdir/postgis-2.3', 'ST_BuildArea';


ALTER FUNCTION public.buildarea(public.geometry) OWNER TO postgres;

--
-- Name: centroid(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.centroid(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'centroid';


ALTER FUNCTION public.centroid(public.geometry) OWNER TO postgres;

--
-- Name: collect(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.collect(public.geometry, public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE
    AS '$libdir/postgis-2.3', 'LWGEOM_collect';


ALTER FUNCTION public.collect(public.geometry, public.geometry) OWNER TO postgres;

--
-- Name: combine_bbox(public.box2d, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.combine_bbox(public.box2d, public.geometry) RETURNS public.box2d
    LANGUAGE c IMMUTABLE
    AS '$libdir/postgis-2.3', 'BOX2D_combine';


ALTER FUNCTION public.combine_bbox(public.box2d, public.geometry) OWNER TO postgres;

--
-- Name: combine_bbox(public.box3d, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.combine_bbox(public.box3d, public.geometry) RETURNS public.box3d
    LANGUAGE c IMMUTABLE
    AS '$libdir/postgis-2.3', 'BOX3D_combine';


ALTER FUNCTION public.combine_bbox(public.box3d, public.geometry) OWNER TO postgres;

--
-- Name: contains(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.contains(public.geometry, public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'contains';


ALTER FUNCTION public.contains(public.geometry, public.geometry) OWNER TO postgres;

--
-- Name: convexhull(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.convexhull(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT COST 100
    AS '$libdir/postgis-2.3', 'convexhull';


ALTER FUNCTION public.convexhull(public.geometry) OWNER TO postgres;

--
-- Name: crosses(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.crosses(public.geometry, public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'crosses';


ALTER FUNCTION public.crosses(public.geometry, public.geometry) OWNER TO postgres;

--
-- Name: difference(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.difference(public.geometry, public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'difference';


ALTER FUNCTION public.difference(public.geometry, public.geometry) OWNER TO postgres;

--
-- Name: dimension(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.dimension(public.geometry) RETURNS integer
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_dimension';


ALTER FUNCTION public.dimension(public.geometry) OWNER TO postgres;

--
-- Name: disjoint(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.disjoint(public.geometry, public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'disjoint';


ALTER FUNCTION public.disjoint(public.geometry, public.geometry) OWNER TO postgres;

--
-- Name: distance(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.distance(public.geometry, public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT COST 100
    AS '$libdir/postgis-2.3', 'LWGEOM_mindistance2d';


ALTER FUNCTION public.distance(public.geometry, public.geometry) OWNER TO postgres;

--
-- Name: distance_sphere(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.distance_sphere(public.geometry, public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT COST 100
    AS '$libdir/postgis-2.3', 'LWGEOM_distance_sphere';


ALTER FUNCTION public.distance_sphere(public.geometry, public.geometry) OWNER TO postgres;

--
-- Name: distance_spheroid(public.geometry, public.geometry, public.spheroid); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.distance_spheroid(public.geometry, public.geometry, public.spheroid) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT COST 100
    AS '$libdir/postgis-2.3', 'LWGEOM_distance_ellipsoid';


ALTER FUNCTION public.distance_spheroid(public.geometry, public.geometry, public.spheroid) OWNER TO postgres;

--
-- Name: dropbbox(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.dropbbox(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_dropBBOX';


ALTER FUNCTION public.dropbbox(public.geometry) OWNER TO postgres;

--
-- Name: dump(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.dump(public.geometry) RETURNS SETOF public.geometry_dump
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_dump';


ALTER FUNCTION public.dump(public.geometry) OWNER TO postgres;

--
-- Name: dumprings(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.dumprings(public.geometry) RETURNS SETOF public.geometry_dump
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_dump_rings';


ALTER FUNCTION public.dumprings(public.geometry) OWNER TO postgres;

--
-- Name: endpoint(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.endpoint(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_endpoint_linestring';


ALTER FUNCTION public.endpoint(public.geometry) OWNER TO postgres;

--
-- Name: envelope(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.envelope(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_envelope';


ALTER FUNCTION public.envelope(public.geometry) OWNER TO postgres;

--
-- Name: estimated_extent(text, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.estimated_extent(text, text) RETURNS public.box2d
    LANGUAGE c IMMUTABLE STRICT SECURITY DEFINER
    AS '$libdir/postgis-2.3', 'geometry_estimated_extent';


ALTER FUNCTION public.estimated_extent(text, text) OWNER TO postgres;

--
-- Name: estimated_extent(text, text, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.estimated_extent(text, text, text) RETURNS public.box2d
    LANGUAGE c IMMUTABLE STRICT SECURITY DEFINER
    AS '$libdir/postgis-2.3', 'geometry_estimated_extent';


ALTER FUNCTION public.estimated_extent(text, text, text) OWNER TO postgres;

--
-- Name: expand(public.box2d, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.expand(public.box2d, double precision) RETURNS public.box2d
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'BOX2D_expand';


ALTER FUNCTION public.expand(public.box2d, double precision) OWNER TO postgres;

--
-- Name: expand(public.box3d, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.expand(public.box3d, double precision) RETURNS public.box3d
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'BOX3D_expand';


ALTER FUNCTION public.expand(public.box3d, double precision) OWNER TO postgres;

--
-- Name: expand(public.geometry, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.expand(public.geometry, double precision) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_expand';


ALTER FUNCTION public.expand(public.geometry, double precision) OWNER TO postgres;

--
-- Name: exteriorring(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.exteriorring(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_exteriorring_polygon';


ALTER FUNCTION public.exteriorring(public.geometry) OWNER TO postgres;

--
-- Name: fib_track_info(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.fib_track_info() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
begin
 new.id := nextval( 'public.seq_tif' );
 return new;
end;
$$;


ALTER FUNCTION public.fib_track_info() OWNER TO postgres;

--
-- Name: find_extent(text, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.find_extent(text, text) RETURNS public.box2d
    LANGUAGE plpgsql IMMUTABLE STRICT
    AS $_$
DECLARE
	tablename alias for $1;
	columnname alias for $2;
	myrec RECORD;

BEGIN
	FOR myrec IN EXECUTE 'SELECT ST_Extent("' || columnname || '") As extent FROM "' || tablename || '"' LOOP
		return myrec.extent;
	END LOOP;
END;
$_$;


ALTER FUNCTION public.find_extent(text, text) OWNER TO postgres;

--
-- Name: find_extent(text, text, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.find_extent(text, text, text) RETURNS public.box2d
    LANGUAGE plpgsql IMMUTABLE STRICT
    AS $_$
DECLARE
	schemaname alias for $1;
	tablename alias for $2;
	columnname alias for $3;
	myrec RECORD;

BEGIN
	FOR myrec IN EXECUTE 'SELECT ST_Extent("' || columnname || '") FROM "' || schemaname || '"."' || tablename || '" As extent ' LOOP
		return myrec.extent;
	END LOOP;
END;
$_$;


ALTER FUNCTION public.find_extent(text, text, text) OWNER TO postgres;

--
-- Name: fix_geometry_columns(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.fix_geometry_columns() RETURNS text
    LANGUAGE plpgsql
    AS $$
DECLARE
	mislinked record;
	result text;
	linked integer;
	deleted integer;
	foundschema integer;
BEGIN

	-- Since 7.3 schema support has been added.
	-- Previous postgis versions used to put the database name in
	-- the schema column. This needs to be fixed, so we try to
	-- set the correct schema for each geometry_colums record
	-- looking at table, column, type and srid.
	
	return 'This function is obsolete now that geometry_columns is a view';

END;
$$;


ALTER FUNCTION public.fix_geometry_columns() OWNER TO postgres;

--
-- Name: force_2d(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.force_2d(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_force_2d';


ALTER FUNCTION public.force_2d(public.geometry) OWNER TO postgres;

--
-- Name: force_3d(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.force_3d(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_force_3dz';


ALTER FUNCTION public.force_3d(public.geometry) OWNER TO postgres;

--
-- Name: force_3dm(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.force_3dm(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_force_3dm';


ALTER FUNCTION public.force_3dm(public.geometry) OWNER TO postgres;

--
-- Name: force_3dz(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.force_3dz(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_force_3dz';


ALTER FUNCTION public.force_3dz(public.geometry) OWNER TO postgres;

--
-- Name: force_4d(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.force_4d(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_force_4d';


ALTER FUNCTION public.force_4d(public.geometry) OWNER TO postgres;

--
-- Name: force_collection(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.force_collection(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_force_collection';


ALTER FUNCTION public.force_collection(public.geometry) OWNER TO postgres;

--
-- Name: forcerhr(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.forcerhr(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_force_clockwise_poly';


ALTER FUNCTION public.forcerhr(public.geometry) OWNER TO postgres;

--
-- Name: geomcollfromtext(text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geomcollfromtext(text) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE
	WHEN geometrytype(GeomFromText($1)) = 'GEOMETRYCOLLECTION'
	THEN GeomFromText($1)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.geomcollfromtext(text) OWNER TO postgres;

--
-- Name: geomcollfromtext(text, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geomcollfromtext(text, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE
	WHEN geometrytype(GeomFromText($1, $2)) = 'GEOMETRYCOLLECTION'
	THEN GeomFromText($1,$2)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.geomcollfromtext(text, integer) OWNER TO postgres;

--
-- Name: geomcollfromwkb(bytea); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geomcollfromwkb(bytea) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE
	WHEN geometrytype(GeomFromWKB($1)) = 'GEOMETRYCOLLECTION'
	THEN GeomFromWKB($1)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.geomcollfromwkb(bytea) OWNER TO postgres;

--
-- Name: geomcollfromwkb(bytea, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geomcollfromwkb(bytea, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE
	WHEN geometrytype(GeomFromWKB($1, $2)) = 'GEOMETRYCOLLECTION'
	THEN GeomFromWKB($1, $2)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.geomcollfromwkb(bytea, integer) OWNER TO postgres;

--
-- Name: geometryfromtext(text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geometryfromtext(text) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_from_text';


ALTER FUNCTION public.geometryfromtext(text) OWNER TO postgres;

--
-- Name: geometryfromtext(text, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geometryfromtext(text, integer) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_from_text';


ALTER FUNCTION public.geometryfromtext(text, integer) OWNER TO postgres;

--
-- Name: geometryn(public.geometry, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geometryn(public.geometry, integer) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_geometryn_collection';


ALTER FUNCTION public.geometryn(public.geometry, integer) OWNER TO postgres;

--
-- Name: geomfromtext(text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geomfromtext(text) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT ST_GeomFromText($1)$_$;


ALTER FUNCTION public.geomfromtext(text) OWNER TO postgres;

--
-- Name: geomfromtext(text, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geomfromtext(text, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT ST_GeomFromText($1, $2)$_$;


ALTER FUNCTION public.geomfromtext(text, integer) OWNER TO postgres;

--
-- Name: geomfromwkb(bytea); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geomfromwkb(bytea) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_from_WKB';


ALTER FUNCTION public.geomfromwkb(bytea) OWNER TO postgres;

--
-- Name: geomfromwkb(bytea, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geomfromwkb(bytea, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT ST_SetSRID(ST_GeomFromWKB($1), $2)$_$;


ALTER FUNCTION public.geomfromwkb(bytea, integer) OWNER TO postgres;

--
-- Name: geomunion(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geomunion(public.geometry, public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'geomunion';


ALTER FUNCTION public.geomunion(public.geometry, public.geometry) OWNER TO postgres;

--
-- Name: getbbox(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.getbbox(public.geometry) RETURNS public.box2d
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_to_BOX2D';


ALTER FUNCTION public.getbbox(public.geometry) OWNER TO postgres;

--
-- Name: getsrid(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.getsrid(public.geometry) RETURNS integer
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_get_srid';


ALTER FUNCTION public.getsrid(public.geometry) OWNER TO postgres;

--
-- Name: hasbbox(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.hasbbox(public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_hasBBOX';


ALTER FUNCTION public.hasbbox(public.geometry) OWNER TO postgres;

--
-- Name: interiorringn(public.geometry, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.interiorringn(public.geometry, integer) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_interiorringn_polygon';


ALTER FUNCTION public.interiorringn(public.geometry, integer) OWNER TO postgres;

--
-- Name: intersection(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.intersection(public.geometry, public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'intersection';


ALTER FUNCTION public.intersection(public.geometry, public.geometry) OWNER TO postgres;

--
-- Name: intersects(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.intersects(public.geometry, public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'intersects';


ALTER FUNCTION public.intersects(public.geometry, public.geometry) OWNER TO postgres;

--
-- Name: isclosed(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.isclosed(public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_isclosed';


ALTER FUNCTION public.isclosed(public.geometry) OWNER TO postgres;

--
-- Name: isempty(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.isempty(public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_isempty';


ALTER FUNCTION public.isempty(public.geometry) OWNER TO postgres;

--
-- Name: isring(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.isring(public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'isring';


ALTER FUNCTION public.isring(public.geometry) OWNER TO postgres;

--
-- Name: issimple(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.issimple(public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'issimple';


ALTER FUNCTION public.issimple(public.geometry) OWNER TO postgres;

--
-- Name: isvalid(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.isvalid(public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT COST 100
    AS '$libdir/postgis-2.3', 'isvalid';


ALTER FUNCTION public.isvalid(public.geometry) OWNER TO postgres;

--
-- Name: length(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.length(public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_length_linestring';


ALTER FUNCTION public.length(public.geometry) OWNER TO postgres;

--
-- Name: length2d(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.length2d(public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_length2d_linestring';


ALTER FUNCTION public.length2d(public.geometry) OWNER TO postgres;

--
-- Name: length2d_spheroid(public.geometry, public.spheroid); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.length2d_spheroid(public.geometry, public.spheroid) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT COST 100
    AS '$libdir/postgis-2.3', 'LWGEOM_length2d_ellipsoid';


ALTER FUNCTION public.length2d_spheroid(public.geometry, public.spheroid) OWNER TO postgres;

--
-- Name: length3d(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.length3d(public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_length_linestring';


ALTER FUNCTION public.length3d(public.geometry) OWNER TO postgres;

--
-- Name: length3d_spheroid(public.geometry, public.spheroid); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.length3d_spheroid(public.geometry, public.spheroid) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_length_ellipsoid_linestring';


ALTER FUNCTION public.length3d_spheroid(public.geometry, public.spheroid) OWNER TO postgres;

--
-- Name: length_spheroid(public.geometry, public.spheroid); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.length_spheroid(public.geometry, public.spheroid) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT COST 100
    AS '$libdir/postgis-2.3', 'LWGEOM_length_ellipsoid_linestring';


ALTER FUNCTION public.length_spheroid(public.geometry, public.spheroid) OWNER TO postgres;

--
-- Name: line_interpolate_point(public.geometry, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.line_interpolate_point(public.geometry, double precision) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_line_interpolate_point';


ALTER FUNCTION public.line_interpolate_point(public.geometry, double precision) OWNER TO postgres;

--
-- Name: line_locate_point(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.line_locate_point(public.geometry, public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_line_locate_point';


ALTER FUNCTION public.line_locate_point(public.geometry, public.geometry) OWNER TO postgres;

--
-- Name: line_substring(public.geometry, double precision, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.line_substring(public.geometry, double precision, double precision) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_line_substring';


ALTER FUNCTION public.line_substring(public.geometry, double precision, double precision) OWNER TO postgres;

--
-- Name: linefrommultipoint(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.linefrommultipoint(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_line_from_mpoint';


ALTER FUNCTION public.linefrommultipoint(public.geometry) OWNER TO postgres;

--
-- Name: linefromtext(text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.linefromtext(text) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromText($1)) = 'LINESTRING'
	THEN GeomFromText($1)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.linefromtext(text) OWNER TO postgres;

--
-- Name: linefromtext(text, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.linefromtext(text, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromText($1, $2)) = 'LINESTRING'
	THEN GeomFromText($1,$2)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.linefromtext(text, integer) OWNER TO postgres;

--
-- Name: linefromwkb(bytea); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.linefromwkb(bytea) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1)) = 'LINESTRING'
	THEN GeomFromWKB($1)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.linefromwkb(bytea) OWNER TO postgres;

--
-- Name: linefromwkb(bytea, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.linefromwkb(bytea, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1, $2)) = 'LINESTRING'
	THEN GeomFromWKB($1, $2)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.linefromwkb(bytea, integer) OWNER TO postgres;

--
-- Name: linemerge(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.linemerge(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT COST 100
    AS '$libdir/postgis-2.3', 'linemerge';


ALTER FUNCTION public.linemerge(public.geometry) OWNER TO postgres;

--
-- Name: linestringfromtext(text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.linestringfromtext(text) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT LineFromText($1)$_$;


ALTER FUNCTION public.linestringfromtext(text) OWNER TO postgres;

--
-- Name: linestringfromtext(text, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.linestringfromtext(text, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT LineFromText($1, $2)$_$;


ALTER FUNCTION public.linestringfromtext(text, integer) OWNER TO postgres;

--
-- Name: linestringfromwkb(bytea); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.linestringfromwkb(bytea) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1)) = 'LINESTRING'
	THEN GeomFromWKB($1)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.linestringfromwkb(bytea) OWNER TO postgres;

--
-- Name: linestringfromwkb(bytea, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.linestringfromwkb(bytea, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1, $2)) = 'LINESTRING'
	THEN GeomFromWKB($1, $2)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.linestringfromwkb(bytea, integer) OWNER TO postgres;

--
-- Name: locate_along_measure(public.geometry, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.locate_along_measure(public.geometry, double precision) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$ SELECT ST_locate_between_measures($1, $2, $2) $_$;


ALTER FUNCTION public.locate_along_measure(public.geometry, double precision) OWNER TO postgres;

--
-- Name: locate_between_measures(public.geometry, double precision, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.locate_between_measures(public.geometry, double precision, double precision) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_locate_between_m';


ALTER FUNCTION public.locate_between_measures(public.geometry, double precision, double precision) OWNER TO postgres;

--
-- Name: m(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.m(public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_m_point';


ALTER FUNCTION public.m(public.geometry) OWNER TO postgres;

--
-- Name: makebox2d(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.makebox2d(public.geometry, public.geometry) RETURNS public.box2d
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'BOX2D_construct';


ALTER FUNCTION public.makebox2d(public.geometry, public.geometry) OWNER TO postgres;

--
-- Name: makebox3d(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.makebox3d(public.geometry, public.geometry) RETURNS public.box3d
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'BOX3D_construct';


ALTER FUNCTION public.makebox3d(public.geometry, public.geometry) OWNER TO postgres;

--
-- Name: makeline(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.makeline(public.geometry, public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_makeline';


ALTER FUNCTION public.makeline(public.geometry, public.geometry) OWNER TO postgres;

--
-- Name: makeline_garray(public.geometry[]); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.makeline_garray(public.geometry[]) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_makeline_garray';


ALTER FUNCTION public.makeline_garray(public.geometry[]) OWNER TO postgres;

--
-- Name: makepoint(double precision, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.makepoint(double precision, double precision) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_makepoint';


ALTER FUNCTION public.makepoint(double precision, double precision) OWNER TO postgres;

--
-- Name: makepoint(double precision, double precision, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.makepoint(double precision, double precision, double precision) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_makepoint';


ALTER FUNCTION public.makepoint(double precision, double precision, double precision) OWNER TO postgres;

--
-- Name: makepoint(double precision, double precision, double precision, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.makepoint(double precision, double precision, double precision, double precision) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_makepoint';


ALTER FUNCTION public.makepoint(double precision, double precision, double precision, double precision) OWNER TO postgres;

--
-- Name: makepointm(double precision, double precision, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.makepointm(double precision, double precision, double precision) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_makepoint3dm';


ALTER FUNCTION public.makepointm(double precision, double precision, double precision) OWNER TO postgres;

--
-- Name: makepolygon(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.makepolygon(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_makepoly';


ALTER FUNCTION public.makepolygon(public.geometry) OWNER TO postgres;

--
-- Name: makepolygon(public.geometry, public.geometry[]); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.makepolygon(public.geometry, public.geometry[]) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_makepoly';


ALTER FUNCTION public.makepolygon(public.geometry, public.geometry[]) OWNER TO postgres;

--
-- Name: max_distance(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.max_distance(public.geometry, public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_maxdistance2d_linestring';


ALTER FUNCTION public.max_distance(public.geometry, public.geometry) OWNER TO postgres;

--
-- Name: mem_size(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.mem_size(public.geometry) RETURNS integer
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_mem_size';


ALTER FUNCTION public.mem_size(public.geometry) OWNER TO postgres;

--
-- Name: mlinefromtext(text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.mlinefromtext(text) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromText($1)) = 'MULTILINESTRING'
	THEN GeomFromText($1)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.mlinefromtext(text) OWNER TO postgres;

--
-- Name: mlinefromtext(text, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.mlinefromtext(text, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE
	WHEN geometrytype(GeomFromText($1, $2)) = 'MULTILINESTRING'
	THEN GeomFromText($1,$2)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.mlinefromtext(text, integer) OWNER TO postgres;

--
-- Name: mlinefromwkb(bytea); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.mlinefromwkb(bytea) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1)) = 'MULTILINESTRING'
	THEN GeomFromWKB($1)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.mlinefromwkb(bytea) OWNER TO postgres;

--
-- Name: mlinefromwkb(bytea, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.mlinefromwkb(bytea, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1, $2)) = 'MULTILINESTRING'
	THEN GeomFromWKB($1, $2)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.mlinefromwkb(bytea, integer) OWNER TO postgres;

--
-- Name: mpointfromtext(text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.mpointfromtext(text) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromText($1)) = 'MULTIPOINT'
	THEN GeomFromText($1)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.mpointfromtext(text) OWNER TO postgres;

--
-- Name: mpointfromtext(text, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.mpointfromtext(text, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromText($1,$2)) = 'MULTIPOINT'
	THEN GeomFromText($1,$2)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.mpointfromtext(text, integer) OWNER TO postgres;

--
-- Name: mpointfromwkb(bytea); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.mpointfromwkb(bytea) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1)) = 'MULTIPOINT'
	THEN GeomFromWKB($1)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.mpointfromwkb(bytea) OWNER TO postgres;

--
-- Name: mpointfromwkb(bytea, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.mpointfromwkb(bytea, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1,$2)) = 'MULTIPOINT'
	THEN GeomFromWKB($1, $2)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.mpointfromwkb(bytea, integer) OWNER TO postgres;

--
-- Name: mpolyfromtext(text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.mpolyfromtext(text) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromText($1)) = 'MULTIPOLYGON'
	THEN GeomFromText($1)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.mpolyfromtext(text) OWNER TO postgres;

--
-- Name: mpolyfromtext(text, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.mpolyfromtext(text, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromText($1, $2)) = 'MULTIPOLYGON'
	THEN GeomFromText($1,$2)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.mpolyfromtext(text, integer) OWNER TO postgres;

--
-- Name: mpolyfromwkb(bytea); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.mpolyfromwkb(bytea) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1)) = 'MULTIPOLYGON'
	THEN GeomFromWKB($1)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.mpolyfromwkb(bytea) OWNER TO postgres;

--
-- Name: mpolyfromwkb(bytea, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.mpolyfromwkb(bytea, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1, $2)) = 'MULTIPOLYGON'
	THEN GeomFromWKB($1, $2)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.mpolyfromwkb(bytea, integer) OWNER TO postgres;

--
-- Name: multi(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.multi(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_force_multi';


ALTER FUNCTION public.multi(public.geometry) OWNER TO postgres;

--
-- Name: multilinefromwkb(bytea); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.multilinefromwkb(bytea) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1)) = 'MULTILINESTRING'
	THEN GeomFromWKB($1)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.multilinefromwkb(bytea) OWNER TO postgres;

--
-- Name: multilinefromwkb(bytea, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.multilinefromwkb(bytea, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1, $2)) = 'MULTILINESTRING'
	THEN GeomFromWKB($1, $2)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.multilinefromwkb(bytea, integer) OWNER TO postgres;

--
-- Name: multilinestringfromtext(text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.multilinestringfromtext(text) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT ST_MLineFromText($1)$_$;


ALTER FUNCTION public.multilinestringfromtext(text) OWNER TO postgres;

--
-- Name: multilinestringfromtext(text, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.multilinestringfromtext(text, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT MLineFromText($1, $2)$_$;


ALTER FUNCTION public.multilinestringfromtext(text, integer) OWNER TO postgres;

--
-- Name: multipointfromtext(text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.multipointfromtext(text) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT MPointFromText($1)$_$;


ALTER FUNCTION public.multipointfromtext(text) OWNER TO postgres;

--
-- Name: multipointfromtext(text, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.multipointfromtext(text, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT MPointFromText($1, $2)$_$;


ALTER FUNCTION public.multipointfromtext(text, integer) OWNER TO postgres;

--
-- Name: multipointfromwkb(bytea); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.multipointfromwkb(bytea) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1)) = 'MULTIPOINT'
	THEN GeomFromWKB($1)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.multipointfromwkb(bytea) OWNER TO postgres;

--
-- Name: multipointfromwkb(bytea, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.multipointfromwkb(bytea, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1,$2)) = 'MULTIPOINT'
	THEN GeomFromWKB($1, $2)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.multipointfromwkb(bytea, integer) OWNER TO postgres;

--
-- Name: multipolyfromwkb(bytea); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.multipolyfromwkb(bytea) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1)) = 'MULTIPOLYGON'
	THEN GeomFromWKB($1)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.multipolyfromwkb(bytea) OWNER TO postgres;

--
-- Name: multipolyfromwkb(bytea, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.multipolyfromwkb(bytea, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1, $2)) = 'MULTIPOLYGON'
	THEN GeomFromWKB($1, $2)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.multipolyfromwkb(bytea, integer) OWNER TO postgres;

--
-- Name: multipolygonfromtext(text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.multipolygonfromtext(text) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT MPolyFromText($1)$_$;


ALTER FUNCTION public.multipolygonfromtext(text) OWNER TO postgres;

--
-- Name: multipolygonfromtext(text, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.multipolygonfromtext(text, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT MPolyFromText($1, $2)$_$;


ALTER FUNCTION public.multipolygonfromtext(text, integer) OWNER TO postgres;

--
-- Name: ndims(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.ndims(public.geometry) RETURNS smallint
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_ndims';


ALTER FUNCTION public.ndims(public.geometry) OWNER TO postgres;

--
-- Name: noop(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.noop(public.geometry) RETURNS public.geometry
    LANGUAGE c STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_noop';


ALTER FUNCTION public.noop(public.geometry) OWNER TO postgres;

--
-- Name: npoints(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.npoints(public.geometry) RETURNS integer
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_npoints';


ALTER FUNCTION public.npoints(public.geometry) OWNER TO postgres;

--
-- Name: nrings(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.nrings(public.geometry) RETURNS integer
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_nrings';


ALTER FUNCTION public.nrings(public.geometry) OWNER TO postgres;

--
-- Name: numgeometries(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.numgeometries(public.geometry) RETURNS integer
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_numgeometries_collection';


ALTER FUNCTION public.numgeometries(public.geometry) OWNER TO postgres;

--
-- Name: numinteriorring(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.numinteriorring(public.geometry) RETURNS integer
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_numinteriorrings_polygon';


ALTER FUNCTION public.numinteriorring(public.geometry) OWNER TO postgres;

--
-- Name: numinteriorrings(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.numinteriorrings(public.geometry) RETURNS integer
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_numinteriorrings_polygon';


ALTER FUNCTION public.numinteriorrings(public.geometry) OWNER TO postgres;

--
-- Name: numpoints(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.numpoints(public.geometry) RETURNS integer
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_numpoints_linestring';


ALTER FUNCTION public.numpoints(public.geometry) OWNER TO postgres;

--
-- Name: overlaps(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public."overlaps"(public.geometry, public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'overlaps';


ALTER FUNCTION public."overlaps"(public.geometry, public.geometry) OWNER TO postgres;

--
-- Name: perimeter2d(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.perimeter2d(public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_perimeter2d_poly';


ALTER FUNCTION public.perimeter2d(public.geometry) OWNER TO postgres;

--
-- Name: perimeter3d(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.perimeter3d(public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_perimeter_poly';


ALTER FUNCTION public.perimeter3d(public.geometry) OWNER TO postgres;

--
-- Name: point_inside_circle(public.geometry, double precision, double precision, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.point_inside_circle(public.geometry, double precision, double precision, double precision) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_inside_circle_point';


ALTER FUNCTION public.point_inside_circle(public.geometry, double precision, double precision, double precision) OWNER TO postgres;

--
-- Name: pointfromtext(text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.pointfromtext(text) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromText($1)) = 'POINT'
	THEN GeomFromText($1)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.pointfromtext(text) OWNER TO postgres;

--
-- Name: pointfromtext(text, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.pointfromtext(text, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromText($1, $2)) = 'POINT'
	THEN GeomFromText($1,$2)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.pointfromtext(text, integer) OWNER TO postgres;

--
-- Name: pointfromwkb(bytea); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.pointfromwkb(bytea) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1)) = 'POINT'
	THEN GeomFromWKB($1)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.pointfromwkb(bytea) OWNER TO postgres;

--
-- Name: pointfromwkb(bytea, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.pointfromwkb(bytea, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(ST_GeomFromWKB($1, $2)) = 'POINT'
	THEN GeomFromWKB($1, $2)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.pointfromwkb(bytea, integer) OWNER TO postgres;

--
-- Name: pointn(public.geometry, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.pointn(public.geometry, integer) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_pointn_linestring';


ALTER FUNCTION public.pointn(public.geometry, integer) OWNER TO postgres;

--
-- Name: pointonsurface(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.pointonsurface(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'pointonsurface';


ALTER FUNCTION public.pointonsurface(public.geometry) OWNER TO postgres;

--
-- Name: polyfromtext(text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.polyfromtext(text) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromText($1)) = 'POLYGON'
	THEN GeomFromText($1)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.polyfromtext(text) OWNER TO postgres;

--
-- Name: polyfromtext(text, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.polyfromtext(text, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromText($1, $2)) = 'POLYGON'
	THEN GeomFromText($1,$2)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.polyfromtext(text, integer) OWNER TO postgres;

--
-- Name: polyfromwkb(bytea); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.polyfromwkb(bytea) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1)) = 'POLYGON'
	THEN GeomFromWKB($1)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.polyfromwkb(bytea) OWNER TO postgres;

--
-- Name: polyfromwkb(bytea, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.polyfromwkb(bytea, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1, $2)) = 'POLYGON'
	THEN GeomFromWKB($1, $2)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.polyfromwkb(bytea, integer) OWNER TO postgres;

--
-- Name: polygonfromtext(text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.polygonfromtext(text) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT PolyFromText($1)$_$;


ALTER FUNCTION public.polygonfromtext(text) OWNER TO postgres;

--
-- Name: polygonfromtext(text, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.polygonfromtext(text, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT PolyFromText($1, $2)$_$;


ALTER FUNCTION public.polygonfromtext(text, integer) OWNER TO postgres;

--
-- Name: polygonfromwkb(bytea); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.polygonfromwkb(bytea) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1)) = 'POLYGON'
	THEN GeomFromWKB($1)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.polygonfromwkb(bytea) OWNER TO postgres;

--
-- Name: polygonfromwkb(bytea, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.polygonfromwkb(bytea, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1,$2)) = 'POLYGON'
	THEN GeomFromWKB($1, $2)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.polygonfromwkb(bytea, integer) OWNER TO postgres;

--
-- Name: polygonize_garray(public.geometry[]); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.polygonize_garray(public.geometry[]) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT COST 100
    AS '$libdir/postgis-2.3', 'polygonize_garray';


ALTER FUNCTION public.polygonize_garray(public.geometry[]) OWNER TO postgres;

--
-- Name: probe_geometry_columns(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.probe_geometry_columns() RETURNS text
    LANGUAGE plpgsql
    AS $$
DECLARE
	inserted integer;
	oldcount integer;
	probed integer;
	stale integer;
BEGIN


	RETURN 'This function is obsolete now that geometry_columns is a view';
END

$$;


ALTER FUNCTION public.probe_geometry_columns() OWNER TO postgres;

--
-- Name: pullfromdepth(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.pullfromdepth() RETURNS integer
    LANGUAGE plpgsql
    AS $$
declare
	iRowId bigint;
	iRplId bigint;
	recRpl depth_tables.rpl_journal_shadow;
	recUtr depth_fdw.user_tracks;
	recTif depth_fdw.track_info;
	iRows integer := 0;
	iMaxId bigint;
	iSeqVal bigint;
	cUser varchar;
begin
	execute 'set session_replication_role = replica';

	select max(id) into iRplId from depth_tables.rpl_journal_shadow;
	if iRplId is null then
		iRplId := 0;
	end if;
	insert into depth_tables.rpl_journal_shadow ( select * from depth_fdw.rpl_journal where id > iRplId );
	
	for recRpl in select * from depth_tables.rpl_journal_shadow where copied is null order by id
	loop
		if recRpl.opcode = 'I' then
			if recRpl.table_name = 'track_info' then
				insert into track_info select * from depth_fdw.track_info where id = recRpl.row_id;
			elsif recRpl.table_name = 'user_tracks' then
				select * into recUtr from depth_fdw.user_tracks where track_id = recRpl.row_id;
				select user_name into cUser from user_profiles where id = recUtr.upr_id;
				insert into user_tracks values( 
					recUtr.track_id,
					cUser,
					recUtr.file_ref,
					recUtr.upload_state,
					recUtr.filetype,
					recUtr.compression,
					recUtr.containertrack,
					recUtr.vesselconfigid,
					recUtr.license,
					recUtr.gauge_name,
					recUtr.gauge,
					recUtr.height_ref,
					recUtr.comment,
					recUtr.watertype,
					recUtr.uploaddate,
					recUtr.bbox,
					recUtr.clusteruuid,
					recUtr.clusterseq,
					recUtr.upr_id,
					recUtr.num_points,
					recUtr.is_container);
--				insert into user_tracks select * from depth_fdw.user_tracks where track_id = recRpl.row_id;
--				update user_tracks set user_name = ( select user_name from user_profiles where upr_id = user_tracks.id ) where track_id = recRpl.row_id;
			end if;
		elsif recRpl.opcode = 'U' then
			if recRpl.table_name = 'user_tracks' then
				select * into recUtr from depth_fdw.user_tracks where track_id = recRpl.row_id;
				update user_tracks set
					num_points = recUtr.num_points,
					upload_state = recUtr.upload_state,
					is_container = recUtr.is_container,
					bbox = recUtr.bbox,
					filetype = recUtr.filetype,
					compression = recUtr.compression
				where track_id = recRpl.row_id;
			elsif recRpl.table_name = 'track_info' then
				select * into recTif from depth_fdw.track_info where id = recRpl.row_id;
				update track_info set 
					short_info = recTif.short_info,
					long_info = recTif.long_info,
					reprocess = recTif.reprocess,
					discard = recTif.discard
				where id = recRpl.row_id;
			end if;
		elsif recRpl.opcode = 'D' then
			if recRpl.table_name = 'track_info' then
				delete from track_info where id = recRpl.row_id;
			end if;
		end if;
		update depth_tables.rpl_journal_shadow set copied = now() where id = recRpl.id;
		iRows := iRows+1;
	end loop;
	
	perform adjustsequence( 'user_tracks_track_id_seq', ( select max( track_id ) from user_tracks ) );
	perform adjustsequence( 'seq_tif', ( select max( id ) from track_info ) );
	
	execute 'set session_replication_role = origin';
	
	return iRows;
end;
$$;


ALTER FUNCTION public.pullfromdepth() OWNER TO postgres;

--
-- Name: relate(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.relate(public.geometry, public.geometry) RETURNS text
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'relate_full';


ALTER FUNCTION public.relate(public.geometry, public.geometry) OWNER TO postgres;

--
-- Name: relate(public.geometry, public.geometry, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.relate(public.geometry, public.geometry, text) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'relate_pattern';


ALTER FUNCTION public.relate(public.geometry, public.geometry, text) OWNER TO postgres;

--
-- Name: removepoint(public.geometry, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.removepoint(public.geometry, integer) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_removepoint';


ALTER FUNCTION public.removepoint(public.geometry, integer) OWNER TO postgres;

--
-- Name: rename_geometry_table_constraints(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.rename_geometry_table_constraints() RETURNS text
    LANGUAGE sql IMMUTABLE
    AS $$
SELECT 'rename_geometry_table_constraint() is obsoleted'::text
$$;


ALTER FUNCTION public.rename_geometry_table_constraints() OWNER TO postgres;

--
-- Name: reverse(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.reverse(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_reverse';


ALTER FUNCTION public.reverse(public.geometry) OWNER TO postgres;

--
-- Name: rotate(public.geometry, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.rotate(public.geometry, double precision) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT st_rotateZ($1, $2)$_$;


ALTER FUNCTION public.rotate(public.geometry, double precision) OWNER TO postgres;

--
-- Name: rotatex(public.geometry, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.rotatex(public.geometry, double precision) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT st_affine($1, 1, 0, 0, 0, cos($2), -sin($2), 0, sin($2), cos($2), 0, 0, 0)$_$;


ALTER FUNCTION public.rotatex(public.geometry, double precision) OWNER TO postgres;

--
-- Name: rotatey(public.geometry, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.rotatey(public.geometry, double precision) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT st_affine($1,  cos($2), 0, sin($2),  0, 1, 0,  -sin($2), 0, cos($2), 0,  0, 0)$_$;


ALTER FUNCTION public.rotatey(public.geometry, double precision) OWNER TO postgres;

--
-- Name: rotatez(public.geometry, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.rotatez(public.geometry, double precision) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT st_affine($1,  cos($2), -sin($2), 0,  sin($2), cos($2), 0,  0, 0, 1,  0, 0, 0)$_$;


ALTER FUNCTION public.rotatez(public.geometry, double precision) OWNER TO postgres;

--
-- Name: rpl_log(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.rpl_log() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
declare
 iId bigint;
begin
 if tg_op = 'DELETE' then
 if tg_table_name = 'user_tracks' then
 iId := old.track_id; 
 else
 iId := old.id; 
 end if;
 else
 if tg_table_name = 'user_tracks' then
 iId := new.track_id; 
 else
 iId := new.id; 
 end if;
 end if;
 
 insert into public.rpl_journal( table_name, row_id, opcode )
values( tg_table_name, iId, substr( tg_op, 1, 1 ) );
 
 return new;
end;
$$;


ALTER FUNCTION public.rpl_log() OWNER TO postgres;

--
-- Name: scale(public.geometry, double precision, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.scale(public.geometry, double precision, double precision) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT st_scale($1, $2, $3, 1)$_$;


ALTER FUNCTION public.scale(public.geometry, double precision, double precision) OWNER TO postgres;

--
-- Name: scale(public.geometry, double precision, double precision, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.scale(public.geometry, double precision, double precision, double precision) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT st_affine($1,  $2, 0, 0,  0, $3, 0,  0, 0, $4,  0, 0, 0)$_$;


ALTER FUNCTION public.scale(public.geometry, double precision, double precision, double precision) OWNER TO postgres;

--
-- Name: se_envelopesintersect(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.se_envelopesintersect(public.geometry, public.geometry) RETURNS boolean
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$ 
	SELECT $1 && $2
	$_$;


ALTER FUNCTION public.se_envelopesintersect(public.geometry, public.geometry) OWNER TO postgres;

--
-- Name: se_is3d(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.se_is3d(public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_hasz';


ALTER FUNCTION public.se_is3d(public.geometry) OWNER TO postgres;

--
-- Name: se_ismeasured(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.se_ismeasured(public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_hasm';


ALTER FUNCTION public.se_ismeasured(public.geometry) OWNER TO postgres;

--
-- Name: se_locatealong(public.geometry, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.se_locatealong(public.geometry, double precision) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$ SELECT SE_LocateBetween($1, $2, $2) $_$;


ALTER FUNCTION public.se_locatealong(public.geometry, double precision) OWNER TO postgres;

--
-- Name: se_locatebetween(public.geometry, double precision, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.se_locatebetween(public.geometry, double precision, double precision) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_locate_between_m';


ALTER FUNCTION public.se_locatebetween(public.geometry, double precision, double precision) OWNER TO postgres;

--
-- Name: se_m(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.se_m(public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_m_point';


ALTER FUNCTION public.se_m(public.geometry) OWNER TO postgres;

--
-- Name: se_z(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.se_z(public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_z_point';


ALTER FUNCTION public.se_z(public.geometry) OWNER TO postgres;

--
-- Name: segmentize(public.geometry, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.segmentize(public.geometry, double precision) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_segmentize2d';


ALTER FUNCTION public.segmentize(public.geometry, double precision) OWNER TO postgres;

--
-- Name: setpoint(public.geometry, integer, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.setpoint(public.geometry, integer, public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_setpoint_linestring';


ALTER FUNCTION public.setpoint(public.geometry, integer, public.geometry) OWNER TO postgres;

--
-- Name: setsrid(public.geometry, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.setsrid(public.geometry, integer) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_set_srid';


ALTER FUNCTION public.setsrid(public.geometry, integer) OWNER TO postgres;

--
-- Name: shift_longitude(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.shift_longitude(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_longitude_shift';


ALTER FUNCTION public.shift_longitude(public.geometry) OWNER TO postgres;

--
-- Name: simplify(public.geometry, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.simplify(public.geometry, double precision) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_simplify2d';


ALTER FUNCTION public.simplify(public.geometry, double precision) OWNER TO postgres;

--
-- Name: snaptogrid(public.geometry, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.snaptogrid(public.geometry, double precision) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT ST_SnapToGrid($1, 0, 0, $2, $2)$_$;


ALTER FUNCTION public.snaptogrid(public.geometry, double precision) OWNER TO postgres;

--
-- Name: snaptogrid(public.geometry, double precision, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.snaptogrid(public.geometry, double precision, double precision) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT ST_SnapToGrid($1, 0, 0, $2, $3)$_$;


ALTER FUNCTION public.snaptogrid(public.geometry, double precision, double precision) OWNER TO postgres;

--
-- Name: snaptogrid(public.geometry, double precision, double precision, double precision, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.snaptogrid(public.geometry, double precision, double precision, double precision, double precision) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_snaptogrid';


ALTER FUNCTION public.snaptogrid(public.geometry, double precision, double precision, double precision, double precision) OWNER TO postgres;

--
-- Name: snaptogrid(public.geometry, public.geometry, double precision, double precision, double precision, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.snaptogrid(public.geometry, public.geometry, double precision, double precision, double precision, double precision) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_snaptogrid_pointoff';


ALTER FUNCTION public.snaptogrid(public.geometry, public.geometry, double precision, double precision, double precision, double precision) OWNER TO postgres;

--
-- Name: srid(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.srid(public.geometry) RETURNS integer
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_get_srid';


ALTER FUNCTION public.srid(public.geometry) OWNER TO postgres;

--
-- Name: st_asbinary(text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_asbinary(text) RETURNS bytea
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$ SELECT ST_AsBinary($1::geometry);$_$;


ALTER FUNCTION public.st_asbinary(text) OWNER TO postgres;

--
-- Name: st_astext(bytea); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_astext(bytea) RETURNS text
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$ SELECT ST_AsText($1::geometry);$_$;


ALTER FUNCTION public.st_astext(bytea) OWNER TO postgres;

--
-- Name: st_box(public.box3d); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_box(public.box3d) RETURNS box
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'BOX3D_to_BOX';


ALTER FUNCTION public.st_box(public.box3d) OWNER TO postgres;

--
-- Name: st_box(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_box(public.geometry) RETURNS box
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_to_BOX';


ALTER FUNCTION public.st_box(public.geometry) OWNER TO postgres;

--
-- Name: st_box2d(public.box3d); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_box2d(public.box3d) RETURNS public.box2d
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'BOX3D_to_BOX2D';


ALTER FUNCTION public.st_box2d(public.box3d) OWNER TO postgres;

--
-- Name: st_box2d(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_box2d(public.geometry) RETURNS public.box2d
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_to_BOX2D';


ALTER FUNCTION public.st_box2d(public.geometry) OWNER TO postgres;

--
-- Name: st_box3d(public.box2d); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_box3d(public.box2d) RETURNS public.box3d
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'BOX2D_to_BOX3D';


ALTER FUNCTION public.st_box3d(public.box2d) OWNER TO postgres;

--
-- Name: st_box3d(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_box3d(public.geometry) RETURNS public.box3d
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_to_BOX3D';


ALTER FUNCTION public.st_box3d(public.geometry) OWNER TO postgres;

--
-- Name: st_box3d_in(cstring); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_box3d_in(cstring) RETURNS public.box3d
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'BOX3D_in';


ALTER FUNCTION public.st_box3d_in(cstring) OWNER TO postgres;

--
-- Name: st_box3d_out(public.box3d); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_box3d_out(public.box3d) RETURNS cstring
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'BOX3D_out';


ALTER FUNCTION public.st_box3d_out(public.box3d) OWNER TO postgres;

--
-- Name: st_bytea(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_bytea(public.geometry) RETURNS bytea
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_to_bytea';


ALTER FUNCTION public.st_bytea(public.geometry) OWNER TO postgres;

--
-- Name: st_geometry(bytea); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_geometry(bytea) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_from_bytea';


ALTER FUNCTION public.st_geometry(bytea) OWNER TO postgres;

--
-- Name: st_geometry(text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_geometry(text) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'parse_WKT_lwgeom';


ALTER FUNCTION public.st_geometry(text) OWNER TO postgres;

--
-- Name: st_geometry(public.box2d); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_geometry(public.box2d) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'BOX2D_to_LWGEOM';


ALTER FUNCTION public.st_geometry(public.box2d) OWNER TO postgres;

--
-- Name: st_geometry(public.box3d); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_geometry(public.box3d) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'BOX3D_to_LWGEOM';


ALTER FUNCTION public.st_geometry(public.box3d) OWNER TO postgres;

--
-- Name: st_geometry_cmp(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_geometry_cmp(public.geometry, public.geometry) RETURNS integer
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'lwgeom_cmp';


ALTER FUNCTION public.st_geometry_cmp(public.geometry, public.geometry) OWNER TO postgres;

--
-- Name: st_geometry_eq(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_geometry_eq(public.geometry, public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'lwgeom_eq';


ALTER FUNCTION public.st_geometry_eq(public.geometry, public.geometry) OWNER TO postgres;

--
-- Name: st_geometry_ge(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_geometry_ge(public.geometry, public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'lwgeom_ge';


ALTER FUNCTION public.st_geometry_ge(public.geometry, public.geometry) OWNER TO postgres;

--
-- Name: st_geometry_gt(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_geometry_gt(public.geometry, public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'lwgeom_gt';


ALTER FUNCTION public.st_geometry_gt(public.geometry, public.geometry) OWNER TO postgres;

--
-- Name: st_geometry_le(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_geometry_le(public.geometry, public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'lwgeom_le';


ALTER FUNCTION public.st_geometry_le(public.geometry, public.geometry) OWNER TO postgres;

--
-- Name: st_geometry_lt(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_geometry_lt(public.geometry, public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'lwgeom_lt';


ALTER FUNCTION public.st_geometry_lt(public.geometry, public.geometry) OWNER TO postgres;

--
-- Name: st_length3d(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_length3d(public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_length_linestring';


ALTER FUNCTION public.st_length3d(public.geometry) OWNER TO postgres;

--
-- Name: st_length_spheroid3d(public.geometry, public.spheroid); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_length_spheroid3d(public.geometry, public.spheroid) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT COST 100
    AS '$libdir/postgis-2.3', 'LWGEOM_length_ellipsoid_linestring';


ALTER FUNCTION public.st_length_spheroid3d(public.geometry, public.spheroid) OWNER TO postgres;

--
-- Name: st_makebox3d(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_makebox3d(public.geometry, public.geometry) RETURNS public.box3d
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'BOX3D_construct';


ALTER FUNCTION public.st_makebox3d(public.geometry, public.geometry) OWNER TO postgres;

--
-- Name: st_makeline_garray(public.geometry[]); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_makeline_garray(public.geometry[]) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_makeline_garray';


ALTER FUNCTION public.st_makeline_garray(public.geometry[]) OWNER TO postgres;

--
-- Name: st_perimeter3d(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_perimeter3d(public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_perimeter_poly';


ALTER FUNCTION public.st_perimeter3d(public.geometry) OWNER TO postgres;

--
-- Name: st_polygonize_garray(public.geometry[]); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_polygonize_garray(public.geometry[]) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT COST 100
    AS '$libdir/postgis-2.3', 'polygonize_garray';


ALTER FUNCTION public.st_polygonize_garray(public.geometry[]) OWNER TO postgres;

--
-- Name: st_text(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_text(public.geometry) RETURNS text
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_to_text';


ALTER FUNCTION public.st_text(public.geometry) OWNER TO postgres;

--
-- Name: st_unite_garray(public.geometry[]); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_unite_garray(public.geometry[]) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'pgis_union_geometry_array';


ALTER FUNCTION public.st_unite_garray(public.geometry[]) OWNER TO postgres;

--
-- Name: startpoint(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.startpoint(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_startpoint_linestring';


ALTER FUNCTION public.startpoint(public.geometry) OWNER TO postgres;

--
-- Name: summary(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.summary(public.geometry) RETURNS text
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_summary';


ALTER FUNCTION public.summary(public.geometry) OWNER TO postgres;

--
-- Name: symdifference(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.symdifference(public.geometry, public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'symdifference';


ALTER FUNCTION public.symdifference(public.geometry, public.geometry) OWNER TO postgres;

--
-- Name: symmetricdifference(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.symmetricdifference(public.geometry, public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'symdifference';


ALTER FUNCTION public.symmetricdifference(public.geometry, public.geometry) OWNER TO postgres;

--
-- Name: tif_upr_integrity(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.tif_upr_integrity() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
declare
	iUprId bigint;
begin
	select id into iUprId from user_profiles where user_name = new.user_name;
	new.upr_id := iUprId;
	return new;
end;
$$;


ALTER FUNCTION public.tif_upr_integrity() OWNER TO postgres;

--
-- Name: touches(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.touches(public.geometry, public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'touches';


ALTER FUNCTION public.touches(public.geometry, public.geometry) OWNER TO postgres;

--
-- Name: transform(public.geometry, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.transform(public.geometry, integer) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'transform';


ALTER FUNCTION public.transform(public.geometry, integer) OWNER TO postgres;

--
-- Name: translate(public.geometry, double precision, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.translate(public.geometry, double precision, double precision) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT st_translate($1, $2, $3, 0)$_$;


ALTER FUNCTION public.translate(public.geometry, double precision, double precision) OWNER TO postgres;

--
-- Name: translate(public.geometry, double precision, double precision, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.translate(public.geometry, double precision, double precision, double precision) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT st_affine($1, 1, 0, 0, 0, 1, 0, 0, 0, 1, $2, $3, $4)$_$;


ALTER FUNCTION public.translate(public.geometry, double precision, double precision, double precision) OWNER TO postgres;

--
-- Name: transscale(public.geometry, double precision, double precision, double precision, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.transscale(public.geometry, double precision, double precision, double precision, double precision) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT st_affine($1,  $4, 0, 0,  0, $5, 0,
		0, 0, 1,  $2 * $4, $3 * $5, 0)$_$;


ALTER FUNCTION public.transscale(public.geometry, double precision, double precision, double precision, double precision) OWNER TO postgres;

--
-- Name: unite_garray(public.geometry[]); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.unite_garray(public.geometry[]) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'pgis_union_geometry_array';


ALTER FUNCTION public.unite_garray(public.geometry[]) OWNER TO postgres;

--
-- Name: within(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.within(public.geometry, public.geometry) RETURNS boolean
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT ST_Within($1, $2)$_$;


ALTER FUNCTION public.within(public.geometry, public.geometry) OWNER TO postgres;

--
-- Name: x(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.x(public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_x_point';


ALTER FUNCTION public.x(public.geometry) OWNER TO postgres;

--
-- Name: xmax(public.box3d); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.xmax(public.box3d) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'BOX3D_xmax';


ALTER FUNCTION public.xmax(public.box3d) OWNER TO postgres;

--
-- Name: xmin(public.box3d); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.xmin(public.box3d) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'BOX3D_xmin';


ALTER FUNCTION public.xmin(public.box3d) OWNER TO postgres;

--
-- Name: y(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.y(public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_y_point';


ALTER FUNCTION public.y(public.geometry) OWNER TO postgres;

--
-- Name: ymax(public.box3d); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.ymax(public.box3d) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'BOX3D_ymax';


ALTER FUNCTION public.ymax(public.box3d) OWNER TO postgres;

--
-- Name: ymin(public.box3d); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.ymin(public.box3d) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'BOX3D_ymin';


ALTER FUNCTION public.ymin(public.box3d) OWNER TO postgres;

--
-- Name: z(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.z(public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_z_point';


ALTER FUNCTION public.z(public.geometry) OWNER TO postgres;

--
-- Name: zmax(public.box3d); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.zmax(public.box3d) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'BOX3D_zmax';


ALTER FUNCTION public.zmax(public.box3d) OWNER TO postgres;

--
-- Name: zmflag(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.zmflag(public.geometry) RETURNS smallint
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'LWGEOM_zmflag';


ALTER FUNCTION public.zmflag(public.geometry) OWNER TO postgres;

--
-- Name: zmin(public.box3d); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.zmin(public.box3d) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'BOX3D_zmin';


ALTER FUNCTION public.zmin(public.box3d) OWNER TO postgres;

--
-- Name: accum(public.geometry); Type: AGGREGATE; Schema: public; Owner: postgres
--

CREATE AGGREGATE public.accum(public.geometry) (
    SFUNC = public.pgis_geometry_accum_transfn,
    STYPE = public.pgis_abs,
    FINALFUNC = public.pgis_geometry_accum_finalfn
);


ALTER AGGREGATE public.accum(public.geometry) OWNER TO postgres;

--
-- Name: extent(public.geometry); Type: AGGREGATE; Schema: public; Owner: postgres
--

CREATE AGGREGATE public.extent(public.geometry) (
    SFUNC = public.st_combine_bbox,
    STYPE = public.box3d,
    FINALFUNC = public.box2d
);


ALTER AGGREGATE public.extent(public.geometry) OWNER TO postgres;

--
-- Name: extent3d(public.geometry); Type: AGGREGATE; Schema: public; Owner: postgres
--

CREATE AGGREGATE public.extent3d(public.geometry) (
    SFUNC = public.combine_bbox,
    STYPE = public.box3d
);


ALTER AGGREGATE public.extent3d(public.geometry) OWNER TO postgres;

--
-- Name: makeline(public.geometry); Type: AGGREGATE; Schema: public; Owner: postgres
--

CREATE AGGREGATE public.makeline(public.geometry) (
    SFUNC = public.pgis_geometry_accum_transfn,
    STYPE = public.pgis_abs,
    FINALFUNC = public.pgis_geometry_makeline_finalfn
);


ALTER AGGREGATE public.makeline(public.geometry) OWNER TO postgres;

--
-- Name: memcollect(public.geometry); Type: AGGREGATE; Schema: public; Owner: postgres
--

CREATE AGGREGATE public.memcollect(public.geometry) (
    SFUNC = public.st_collect,
    STYPE = public.geometry
);


ALTER AGGREGATE public.memcollect(public.geometry) OWNER TO postgres;

--
-- Name: memgeomunion(public.geometry); Type: AGGREGATE; Schema: public; Owner: postgres
--

CREATE AGGREGATE public.memgeomunion(public.geometry) (
    SFUNC = public.geomunion,
    STYPE = public.geometry
);


ALTER AGGREGATE public.memgeomunion(public.geometry) OWNER TO postgres;

--
-- Name: st_extent3d(public.geometry); Type: AGGREGATE; Schema: public; Owner: postgres
--

CREATE AGGREGATE public.st_extent3d(public.geometry) (
    SFUNC = public.st_combine_bbox,
    STYPE = public.box3d
);


ALTER AGGREGATE public.st_extent3d(public.geometry) OWNER TO postgres;

--
-- Name: depth; Type: SERVER; Schema: -; Owner: postgres
--

CREATE SERVER depth FOREIGN DATA WRAPPER postgres_fdw OPTIONS (
    dbname 'depth',
    host 'postgis',
    port '5432'
);


ALTER SERVER depth OWNER TO postgres;

--
-- Name: USER MAPPING public SERVER depth; Type: USER MAPPING; Schema: -; Owner: postgres
--

CREATE USER MAPPING FOR public SERVER depth OPTIONS (
    "user" 'osm'
);


SET default_tablespace = '';

--
-- Name: rpl_journal; Type: FOREIGN TABLE; Schema: depth_fdw; Owner: postgres
--

CREATE FOREIGN TABLE depth_fdw.rpl_journal (
    id bigint NOT NULL,
    table_name character varying(50) NOT NULL,
    row_id bigint NOT NULL,
    opcode character varying(1) NOT NULL,
    time_stamp timestamp without time zone NOT NULL
)
SERVER depth
OPTIONS (
    schema_name 'public',
    table_name 'rpl_journal'
);
ALTER FOREIGN TABLE depth_fdw.rpl_journal ALTER COLUMN id OPTIONS (
    column_name 'id'
);
ALTER FOREIGN TABLE depth_fdw.rpl_journal ALTER COLUMN table_name OPTIONS (
    column_name 'table_name'
);
ALTER FOREIGN TABLE depth_fdw.rpl_journal ALTER COLUMN row_id OPTIONS (
    column_name 'row_id'
);
ALTER FOREIGN TABLE depth_fdw.rpl_journal ALTER COLUMN opcode OPTIONS (
    column_name 'opcode'
);
ALTER FOREIGN TABLE depth_fdw.rpl_journal ALTER COLUMN time_stamp OPTIONS (
    column_name 'time_stamp'
);


ALTER FOREIGN TABLE depth_fdw.rpl_journal OWNER TO postgres;

--
-- Name: track_info; Type: FOREIGN TABLE; Schema: depth_fdw; Owner: postgres
--

CREATE FOREIGN TABLE depth_fdw.track_info (
    id bigint NOT NULL,
    tra_id bigint NOT NULL,
    short_info character varying(20),
    long_info character varying,
    reprocess boolean,
    discard boolean,
    ignore boolean
)
SERVER depth
OPTIONS (
    schema_name 'osmapi_tables',
    table_name 'track_info'
);
ALTER FOREIGN TABLE depth_fdw.track_info ALTER COLUMN id OPTIONS (
    column_name 'id'
);
ALTER FOREIGN TABLE depth_fdw.track_info ALTER COLUMN tra_id OPTIONS (
    column_name 'tra_id'
);
ALTER FOREIGN TABLE depth_fdw.track_info ALTER COLUMN short_info OPTIONS (
    column_name 'short_info'
);
ALTER FOREIGN TABLE depth_fdw.track_info ALTER COLUMN long_info OPTIONS (
    column_name 'long_info'
);
ALTER FOREIGN TABLE depth_fdw.track_info ALTER COLUMN reprocess OPTIONS (
    column_name 'reprocess'
);
ALTER FOREIGN TABLE depth_fdw.track_info ALTER COLUMN discard OPTIONS (
    column_name 'discard'
);
ALTER FOREIGN TABLE depth_fdw.track_info ALTER COLUMN ignore OPTIONS (
    column_name 'ignore'
);


ALTER FOREIGN TABLE depth_fdw.track_info OWNER TO postgres;

--
-- Name: user_tracks; Type: FOREIGN TABLE; Schema: depth_fdw; Owner: postgres
--

CREATE FOREIGN TABLE depth_fdw.user_tracks (
    track_id bigint NOT NULL,
    file_ref character varying(255),
    upload_state smallint,
    filetype character varying(80),
    compression character varying(80),
    containertrack integer,
    vesselconfigid integer,
    license integer,
    gauge_name character varying(100),
    gauge numeric(6,2),
    height_ref character varying(100),
    comment character varying,
    watertype character varying(20),
    uploaddate timestamp without time zone,
    bbox public.geometry,
    clusteruuid character varying,
    clusterseq bigint,
    upr_id bigint NOT NULL,
    num_points bigint,
    is_container boolean
)
SERVER depth
OPTIONS (
    schema_name 'osmapi_tables',
    table_name 'user_tracks'
);
ALTER FOREIGN TABLE depth_fdw.user_tracks ALTER COLUMN track_id OPTIONS (
    column_name 'track_id'
);
ALTER FOREIGN TABLE depth_fdw.user_tracks ALTER COLUMN file_ref OPTIONS (
    column_name 'file_ref'
);
ALTER FOREIGN TABLE depth_fdw.user_tracks ALTER COLUMN upload_state OPTIONS (
    column_name 'upload_state'
);
ALTER FOREIGN TABLE depth_fdw.user_tracks ALTER COLUMN filetype OPTIONS (
    column_name 'filetype'
);
ALTER FOREIGN TABLE depth_fdw.user_tracks ALTER COLUMN compression OPTIONS (
    column_name 'compression'
);
ALTER FOREIGN TABLE depth_fdw.user_tracks ALTER COLUMN containertrack OPTIONS (
    column_name 'containertrack'
);
ALTER FOREIGN TABLE depth_fdw.user_tracks ALTER COLUMN vesselconfigid OPTIONS (
    column_name 'vesselconfigid'
);
ALTER FOREIGN TABLE depth_fdw.user_tracks ALTER COLUMN license OPTIONS (
    column_name 'license'
);
ALTER FOREIGN TABLE depth_fdw.user_tracks ALTER COLUMN gauge_name OPTIONS (
    column_name 'gauge_name'
);
ALTER FOREIGN TABLE depth_fdw.user_tracks ALTER COLUMN gauge OPTIONS (
    column_name 'gauge'
);
ALTER FOREIGN TABLE depth_fdw.user_tracks ALTER COLUMN height_ref OPTIONS (
    column_name 'height_ref'
);
ALTER FOREIGN TABLE depth_fdw.user_tracks ALTER COLUMN comment OPTIONS (
    column_name 'comment'
);
ALTER FOREIGN TABLE depth_fdw.user_tracks ALTER COLUMN watertype OPTIONS (
    column_name 'watertype'
);
ALTER FOREIGN TABLE depth_fdw.user_tracks ALTER COLUMN uploaddate OPTIONS (
    column_name 'uploaddate'
);
ALTER FOREIGN TABLE depth_fdw.user_tracks ALTER COLUMN bbox OPTIONS (
    column_name 'bbox'
);
ALTER FOREIGN TABLE depth_fdw.user_tracks ALTER COLUMN clusteruuid OPTIONS (
    column_name 'clusteruuid'
);
ALTER FOREIGN TABLE depth_fdw.user_tracks ALTER COLUMN clusterseq OPTIONS (
    column_name 'clusterseq'
);
ALTER FOREIGN TABLE depth_fdw.user_tracks ALTER COLUMN upr_id OPTIONS (
    column_name 'upr_id'
);
ALTER FOREIGN TABLE depth_fdw.user_tracks ALTER COLUMN num_points OPTIONS (
    column_name 'num_points'
);
ALTER FOREIGN TABLE depth_fdw.user_tracks ALTER COLUMN is_container OPTIONS (
    column_name 'is_container'
);


ALTER FOREIGN TABLE depth_fdw.user_tracks OWNER TO postgres;

SET default_with_oids = false;

--
-- Name: rpl_journal_shadow; Type: TABLE; Schema: depth_tables; Owner: postgres
--

CREATE TABLE depth_tables.rpl_journal_shadow (
    id bigint NOT NULL,
    table_name character varying(50) NOT NULL,
    row_id bigint NOT NULL,
    opcode character varying(1) NOT NULL,
    time_stamp timestamp without time zone NOT NULL,
    copied timestamp without time zone
);


ALTER TABLE depth_tables.rpl_journal_shadow OWNER TO postgres;

--
-- Name: depthsensor_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.depthsensor_id_seq
    START WITH 1
    INCREMENT BY 2
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.depthsensor_id_seq OWNER TO postgres;

--
-- Name: depthsensor; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.depthsensor (
    vesselconfigid integer NOT NULL,
    x numeric(5,2),
    y numeric(5,2),
    z numeric(5,2),
    sensorid character varying,
    manufacturer character varying(100),
    model character varying(100),
    frequency numeric(5,0),
    angleofbeam numeric(3,0),
    offsetkeel numeric(5,2),
    offsettype character varying(12),
    id bigint DEFAULT nextval('public.depthsensor_id_seq'::regclass) NOT NULL
);


ALTER TABLE public.depthsensor OWNER TO postgres;

--
-- Name: gauge; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.gauge (
    id integer NOT NULL,
    name character varying(255) NOT NULL,
    gaugetype character varying(10) DEFAULT 'UNKNOWN'::character varying,
    lat numeric(11,3),
    lon numeric(11,3),
    geom public.geometry,
    provider character varying,
    water character varying,
    remoteid character varying,
    waterlevel numeric(6,2),
    CONSTRAINT enforce_dims_geom CHECK ((public.st_ndims(geom) = 2)),
    CONSTRAINT enforce_geotype_geom CHECK (((public.geometrytype(geom) = 'POINT'::text) OR (geom IS NULL))),
    CONSTRAINT enforce_srid_geom CHECK ((public.st_srid(geom) = 4326))
);


ALTER TABLE public.gauge OWNER TO postgres;

--
-- Name: gauge_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.gauge_id_seq
    START WITH 20
    INCREMENT BY 2
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.gauge_id_seq OWNER TO postgres;

--
-- Name: gaugemeasurement; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.gaugemeasurement (
    gaugeid integer NOT NULL,
    value numeric(4,2) NOT NULL,
    "time" timestamp without time zone NOT NULL
);


ALTER TABLE public.gaugemeasurement OWNER TO postgres;

--
-- Name: license; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.license (
    name character varying(255) NOT NULL,
    shortname character varying(16),
    text text,
    public boolean,
    id integer NOT NULL,
    user_name character varying(255)
);


ALTER TABLE public.license OWNER TO postgres;

--
-- Name: license_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.license_id_seq
    START WITH 1
    INCREMENT BY 2
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.license_id_seq OWNER TO postgres;

--
-- Name: license_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.license_id_seq OWNED BY public.license.id;


--
-- Name: repl_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.repl_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.repl_id_seq OWNER TO postgres;

--
-- Name: rpl_journal; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.rpl_journal (
    id bigint DEFAULT nextval('public.repl_id_seq'::regclass) NOT NULL,
    table_name character varying(50) NOT NULL,
    row_id bigint NOT NULL,
    opcode character varying(1) NOT NULL,
    time_stamp timestamp without time zone DEFAULT now() NOT NULL
);


ALTER TABLE public.rpl_journal OWNER TO postgres;

--
-- Name: sbassensor_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.sbassensor_id_seq
    START WITH 1
    INCREMENT BY 2
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.sbassensor_id_seq OWNER TO postgres;

--
-- Name: sbassensor; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sbassensor (
    vesselconfigid integer NOT NULL,
    x numeric(5,2),
    y numeric(5,2),
    z numeric(5,2),
    sensorid character varying,
    manufacturer character varying(100),
    model character varying(100),
    id bigint DEFAULT nextval('public.sbassensor_id_seq'::regclass) NOT NULL
);


ALTER TABLE public.sbassensor OWNER TO postgres;

--
-- Name: seq_tif; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.seq_tif
    START WITH 1
    INCREMENT BY 2
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_tif OWNER TO postgres;

--
-- Name: tmp_tg_user_profiles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tmp_tg_user_profiles (
    user_name character varying(256),
    password character varying(40),
    salt character varying(10),
    attempts smallint,
    last_attempt timestamp without time zone,
    forename character varying,
    surname character varying,
    country character varying,
    language character varying,
    organisation character varying,
    phone character varying,
    acceptedemailcontact boolean,
    num_tracks integer
);


ALTER TABLE public.tmp_tg_user_profiles OWNER TO postgres;

--
-- Name: tmp_tg_user_tracks_2018_12_03; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tmp_tg_user_tracks_2018_12_03 (
    track_id bigint,
    user_name character varying(40),
    file_ref character varying(255),
    upload_state smallint,
    filetype character varying(80),
    compression character varying(80),
    containertrack integer,
    vesselconfigid integer,
    license integer,
    gauge_name character varying(100),
    gauge numeric(6,2),
    height_ref character varying(100),
    comment character varying,
    watertype character varying(20),
    uploaddate timestamp without time zone,
    bbox public.geometry
);


ALTER TABLE public.tmp_tg_user_tracks_2018_12_03 OWNER TO postgres;

--
-- Name: track_info; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.track_info (
    id bigint DEFAULT nextval('public.seq_tif'::regclass) NOT NULL,
    tra_id bigint NOT NULL,
    short_info character varying(20),
    long_info character varying,
    reprocess boolean,
    discard boolean,
    ignore boolean
);


ALTER TABLE public.track_info OWNER TO postgres;

--
-- Name: trackgauges; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackgauges (
    id integer NOT NULL,
    trackid bigint,
    gaugeid integer,
    source integer
);


ALTER TABLE public.trackgauges OWNER TO postgres;

--
-- Name: trackgauges_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.trackgauges_id_seq
    START WITH 1
    INCREMENT BY 2
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.trackgauges_id_seq OWNER TO postgres;

--
-- Name: trackgauges_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.trackgauges_id_seq OWNED BY public.trackgauges.id;


--
-- Name: user_profiles_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.user_profiles_id_seq
    START WITH 1
    INCREMENT BY 2
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.user_profiles_id_seq OWNER TO postgres;

--
-- Name: user_profiles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_profiles (
    user_name character varying(256) NOT NULL,
    password character varying(40),
    salt character varying(10),
    attempts smallint DEFAULT 0 NOT NULL,
    last_attempt timestamp without time zone,
    forename character varying,
    surname character varying,
    country character varying,
    language character varying,
    organisation character varying,
    phone character varying,
    acceptedemailcontact boolean DEFAULT false,
    id bigint DEFAULT nextval('public.user_profiles_id_seq'::regclass) NOT NULL
);


ALTER TABLE public.user_profiles OWNER TO postgres;

--
-- Name: user_tracks_track_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.user_tracks_track_id_seq
    START WITH 1
    INCREMENT BY 2
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.user_tracks_track_id_seq OWNER TO postgres;

--
-- Name: user_tracks; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_tracks (
    track_id bigint DEFAULT nextval('public.user_tracks_track_id_seq'::regclass) NOT NULL,
    user_name character varying(40) NOT NULL,
    file_ref character varying(255),
    upload_state smallint DEFAULT 0,
    filetype character varying(80),
    compression character varying(80),
    containertrack integer,
    vesselconfigid integer,
    license integer,
    gauge_name character varying(100),
    gauge numeric(6,2),
    height_ref character varying(100),
    comment character varying,
    watertype character varying(20),
    uploaddate timestamp without time zone DEFAULT now(),
    bbox public.geometry,
    clusteruuid character varying,
    clusterseq bigint,
    upr_id bigint NOT NULL,
    num_points integer,
    is_container boolean,
    CONSTRAINT enforce_dims_bbox CHECK ((public.st_ndims(bbox) = 2)),
    CONSTRAINT enforce_geotype_bbox CHECK (((public.geometrytype(bbox) = 'POLYGON'::text) OR (bbox IS NULL))),
    CONSTRAINT enforce_srid_bbox CHECK ((public.st_srid(bbox) = 4326))
);


ALTER TABLE public.user_tracks OWNER TO postgres;

--
-- Name: userroles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.userroles (
    user_name character varying(250),
    role character varying(15)
);


ALTER TABLE public.userroles OWNER TO postgres;

--
-- Name: v_user_tracks; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.v_user_tracks AS
 SELECT user_tracks.track_id,
    user_tracks.user_name,
    user_tracks.file_ref,
    user_tracks.upload_state,
    user_tracks.filetype,
    user_tracks.compression,
    user_tracks.containertrack,
    user_tracks.vesselconfigid,
    user_tracks.license,
    user_tracks.gauge_name,
    user_tracks.gauge,
    user_tracks.height_ref,
    user_tracks.comment,
    user_tracks.watertype,
    user_tracks.uploaddate,
    user_tracks.bbox,
    user_tracks.clusteruuid,
    user_tracks.clusterseq,
    user_tracks.upr_id,
    user_tracks.num_points,
    user_tracks.is_container,
    ( SELECT string_agg((((track_info.short_info)::text || ': '::text) || (track_info.long_info)::text), '\n'::text) AS string_agg
           FROM public.track_info
          WHERE (track_info.tra_id = user_tracks.track_id)) AS track_info,
    public.st_xmin((user_tracks.bbox)::public.box3d) AS "left",
    public.st_xmax((user_tracks.bbox)::public.box3d) AS "right",
    public.st_ymax((user_tracks.bbox)::public.box3d) AS top,
    public.st_ymin((user_tracks.bbox)::public.box3d) AS bottom
   FROM public.user_tracks;


ALTER TABLE public.v_user_tracks OWNER TO postgres;

--
-- Name: vesselconfiguration; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.vesselconfiguration (
    id integer NOT NULL,
    name character varying,
    description character varying,
    user_name character varying,
    mmsi character varying(20),
    manufacturer character varying(100),
    model character varying,
    loa numeric(7,2),
    breadth numeric(7,2),
    draft numeric(4,2),
    height numeric(4,2),
    displacement numeric(8,1),
    maximumspeed numeric(3,1),
    type integer DEFAULT 0,
    upr_id bigint NOT NULL
);


ALTER TABLE public.vesselconfiguration OWNER TO postgres;

--
-- Name: vesselconfiguration_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.vesselconfiguration_id_seq
    START WITH 1
    INCREMENT BY 2
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.vesselconfiguration_id_seq OWNER TO postgres;

--
-- Name: vesselconfiguration_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.vesselconfiguration_id_seq OWNED BY public.vesselconfiguration.id;


--
-- Name: license id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.license ALTER COLUMN id SET DEFAULT nextval('public.license_id_seq'::regclass);


--
-- Name: trackgauges id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackgauges ALTER COLUMN id SET DEFAULT nextval('public.trackgauges_id_seq'::regclass);


--
-- Name: vesselconfiguration id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.vesselconfiguration ALTER COLUMN id SET DEFAULT nextval('public.vesselconfiguration_id_seq'::regclass);


--
-- Name: rpl_journal_shadow pk_rpl_j; Type: CONSTRAINT; Schema: depth_tables; Owner: postgres
--

ALTER TABLE ONLY depth_tables.rpl_journal_shadow
    ADD CONSTRAINT pk_rpl_j PRIMARY KEY (id);


--
-- Name: depthsensor dse_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.depthsensor
    ADD CONSTRAINT dse_pk PRIMARY KEY (id);


--
-- Name: gauge gauge_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.gauge
    ADD CONSTRAINT gauge_pkey PRIMARY KEY (id);


--
-- Name: gaugemeasurement gaugemeasurement_unique; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.gaugemeasurement
    ADD CONSTRAINT gaugemeasurement_unique UNIQUE (gaugeid, "time");


--
-- Name: license license_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.license
    ADD CONSTRAINT license_pkey PRIMARY KEY (id);


--
-- Name: rpl_journal pk_rpl_j; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.rpl_journal
    ADD CONSTRAINT pk_rpl_j PRIMARY KEY (id);


--
-- Name: sbassensor sse_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sbassensor
    ADD CONSTRAINT sse_pk PRIMARY KEY (id);


--
-- Name: track_info tif_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.track_info
    ADD CONSTRAINT tif_pk PRIMARY KEY (id);


--
-- Name: trackgauges trackgauges_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackgauges
    ADD CONSTRAINT trackgauges_pkey PRIMARY KEY (id);


--
-- Name: user_profiles upr_name_uk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_profiles
    ADD CONSTRAINT upr_name_uk UNIQUE (user_name);


--
-- Name: user_profiles upr_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_profiles
    ADD CONSTRAINT upr_pk PRIMARY KEY (id);


--
-- Name: user_tracks user_tracks_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_tracks
    ADD CONSTRAINT user_tracks_pkey PRIMARY KEY (track_id);


--
-- Name: vesselconfiguration vesselconfiguration_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.vesselconfiguration
    ADD CONSTRAINT vesselconfiguration_pkey PRIMARY KEY (id);


--
-- Name: rpl_j_s_id_new; Type: INDEX; Schema: depth_tables; Owner: postgres
--

CREATE INDEX rpl_j_s_id_new ON depth_tables.rpl_journal_shadow USING btree (id) WHERE (copied IS NULL);


--
-- Name: fki_gaugemeasurement_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_gaugemeasurement_fkey ON public.gaugemeasurement USING btree (gaugeid);


--
-- Name: tif_tra_fk_i; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX tif_tra_fk_i ON public.track_info USING btree (tra_id);


--
-- Name: tmp_tg_user_tracks_2018_12_03_tid; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX tmp_tg_user_tracks_2018_12_03_tid ON public.tmp_tg_user_tracks_2018_12_03 USING btree (track_id);


--
-- Name: user_tracks_user; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX user_tracks_user ON public.user_tracks USING btree (user_name);


--
-- Name: utr_upr_fk_i; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX utr_upr_fk_i ON public.user_tracks USING btree (upr_id);


--
-- Name: utr_utr_fk_i; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX utr_utr_fk_i ON public.user_tracks USING btree (containertrack);


--
-- Name: utr_vcf_fk_i; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX utr_vcf_fk_i ON public.user_tracks USING btree (vesselconfigid);


--
-- Name: vcf_upr_fk_i; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX vcf_upr_fk_i ON public.vesselconfiguration USING btree (upr_id);


--
-- Name: depthsensor rpl_log_dse; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER rpl_log_dse AFTER INSERT OR DELETE OR UPDATE ON public.depthsensor FOR EACH ROW EXECUTE PROCEDURE public.rpl_log();


--
-- Name: sbassensor rpl_log_sse; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER rpl_log_sse AFTER INSERT OR DELETE OR UPDATE ON public.sbassensor FOR EACH ROW EXECUTE PROCEDURE public.rpl_log();


--
-- Name: user_profiles rpl_log_upr; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER rpl_log_upr AFTER INSERT OR DELETE OR UPDATE ON public.user_profiles FOR EACH ROW EXECUTE PROCEDURE public.rpl_log();


--
-- Name: user_tracks rpl_log_utr; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER rpl_log_utr AFTER INSERT OR DELETE OR UPDATE ON public.user_tracks FOR EACH ROW EXECUTE PROCEDURE public.rpl_log();


--
-- Name: vesselconfiguration rpl_log_vcf; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER rpl_log_vcf AFTER INSERT OR DELETE OR UPDATE ON public.vesselconfiguration FOR EACH ROW EXECUTE PROCEDURE public.rpl_log();


--
-- Name: user_tracks ti_utr_upr_integrity; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER ti_utr_upr_integrity BEFORE INSERT ON public.user_tracks FOR EACH ROW EXECUTE PROCEDURE public.tif_upr_integrity();


--
-- Name: vesselconfiguration ti_vcf_upr_integrity; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER ti_vcf_upr_integrity BEFORE INSERT ON public.vesselconfiguration FOR EACH ROW EXECUTE PROCEDURE public.tif_upr_integrity();


--
-- Name: depthsensor depthsoffset_vesselconfigid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.depthsensor
    ADD CONSTRAINT depthsoffset_vesselconfigid_fkey FOREIGN KEY (vesselconfigid) REFERENCES public.vesselconfiguration(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: trackgauges gauge_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackgauges
    ADD CONSTRAINT gauge_fkey FOREIGN KEY (gaugeid) REFERENCES public.gauge(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: gaugemeasurement gaugemeasurement_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.gaugemeasurement
    ADD CONSTRAINT gaugemeasurement_fkey FOREIGN KEY (gaugeid) REFERENCES public.gauge(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: sbassensor sbasoffset_vesselconfigid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sbassensor
    ADD CONSTRAINT sbasoffset_vesselconfigid_fkey FOREIGN KEY (vesselconfigid) REFERENCES public.vesselconfiguration(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: track_info tif_tra_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.track_info
    ADD CONSTRAINT tif_tra_fk FOREIGN KEY (tra_id) REFERENCES public.user_tracks(track_id);


--
-- Name: trackgauges trackgauges_trackid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackgauges
    ADD CONSTRAINT trackgauges_trackid_fkey FOREIGN KEY (trackid) REFERENCES public.user_tracks(track_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: user_tracks utr_upr_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_tracks
    ADD CONSTRAINT utr_upr_fk FOREIGN KEY (upr_id) REFERENCES public.user_profiles(id) ON DELETE CASCADE;


--
-- Name: user_tracks utr_utr_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_tracks
    ADD CONSTRAINT utr_utr_fk FOREIGN KEY (containertrack) REFERENCES public.user_tracks(track_id);


--
-- Name: user_tracks utr_vcf_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_tracks
    ADD CONSTRAINT utr_vcf_fk FOREIGN KEY (vesselconfigid) REFERENCES public.vesselconfiguration(id);


--
-- Name: vesselconfiguration vcf_upr_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.vesselconfiguration
    ADD CONSTRAINT vcf_upr_fk FOREIGN KEY (upr_id) REFERENCES public.user_profiles(id) ON DELETE CASCADE;


--
-- Name: SEQUENCE depthsensor_id_seq; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT,UPDATE ON SEQUENCE public.depthsensor_id_seq TO PUBLIC;


--
-- Name: TABLE depthsensor; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT,INSERT,UPDATE ON TABLE public.depthsensor TO osmapi;


--
-- Name: TABLE gauge; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT,INSERT,UPDATE ON TABLE public.gauge TO osmapi;


--
-- Name: TABLE gaugemeasurement; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT,INSERT,UPDATE ON TABLE public.gaugemeasurement TO osmapi;


--
-- Name: TABLE geography_columns; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT,INSERT,UPDATE ON TABLE public.geography_columns TO osmapi;


--
-- Name: TABLE geometry_columns; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT,INSERT,UPDATE ON TABLE public.geometry_columns TO osmapi;


--
-- Name: TABLE license; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT,INSERT,UPDATE ON TABLE public.license TO osmapi;


--
-- Name: TABLE raster_columns; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT,INSERT,UPDATE ON TABLE public.raster_columns TO osmapi;


--
-- Name: TABLE raster_overviews; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT,INSERT,UPDATE ON TABLE public.raster_overviews TO osmapi;


--
-- Name: SEQUENCE repl_id_seq; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT,UPDATE ON SEQUENCE public.repl_id_seq TO osmapi;
GRANT SELECT,UPDATE ON SEQUENCE public.repl_id_seq TO osm;


--
-- Name: TABLE rpl_journal; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT,INSERT ON TABLE public.rpl_journal TO osmapi;
GRANT SELECT,INSERT ON TABLE public.rpl_journal TO osm;


--
-- Name: SEQUENCE sbassensor_id_seq; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT,UPDATE ON SEQUENCE public.sbassensor_id_seq TO PUBLIC;


--
-- Name: TABLE sbassensor; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT,INSERT,UPDATE ON TABLE public.sbassensor TO osmapi;


--
-- Name: SEQUENCE seq_tif; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT,UPDATE ON SEQUENCE public.seq_tif TO osmapi;
GRANT SELECT,UPDATE ON SEQUENCE public.seq_tif TO osm;


--
-- Name: TABLE spatial_ref_sys; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT,INSERT,UPDATE ON TABLE public.spatial_ref_sys TO osmapi;


--
-- Name: TABLE track_info; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.track_info TO osm;
GRANT ALL ON TABLE public.track_info TO osmapi;


--
-- Name: TABLE trackgauges; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT,INSERT,UPDATE ON TABLE public.trackgauges TO osmapi;


--
-- Name: SEQUENCE user_profiles_id_seq; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON SEQUENCE public.user_profiles_id_seq TO PUBLIC;


--
-- Name: TABLE user_profiles; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT,INSERT,UPDATE ON TABLE public.user_profiles TO osmapi;


--
-- Name: SEQUENCE user_tracks_track_id_seq; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON SEQUENCE public.user_tracks_track_id_seq TO osmapi;
GRANT ALL ON SEQUENCE public.user_tracks_track_id_seq TO osm;


--
-- Name: TABLE user_tracks; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.user_tracks TO osmapi;


--
-- Name: TABLE userroles; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT,INSERT,UPDATE ON TABLE public.userroles TO osmapi;


--
-- Name: TABLE v_user_tracks; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.v_user_tracks TO PUBLIC;


--
-- Name: TABLE vesselconfiguration; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT,INSERT,UPDATE ON TABLE public.vesselconfiguration TO osmapi;


--
-- Name: SEQUENCE vesselconfiguration_id_seq; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT,UPDATE ON SEQUENCE public.vesselconfiguration_id_seq TO osmapi;


--
-- PostgreSQL database dump complete
--

