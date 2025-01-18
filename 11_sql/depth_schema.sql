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
-- Name: osmapi_fdw; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA osmapi_fdw;


ALTER SCHEMA osmapi_fdw OWNER TO postgres;

--
-- Name: osmapi_tables; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA osmapi_tables;


ALTER SCHEMA osmapi_tables OWNER TO postgres;

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
-- Name: merged_trackpoint; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.merged_trackpoint AS (
	lat double precision,
	lon double precision,
	depth double precision,
	num_points double precision
);


ALTER TYPE public.merged_trackpoint OWNER TO postgres;

--
-- Name: pullfromosmapi(); Type: FUNCTION; Schema: osmapi_tables; Owner: postgres
--

CREATE FUNCTION osmapi_tables.pullfromosmapi() RETURNS integer
    LANGUAGE plpgsql
    AS $$
declare
	iRowId bigint;
	iRplId bigint;
	recRpl osmapi_tables.rpl_journal_shadow;
	recUtr osmapi_fdw.user_tracks;
	iRows integer := 0;
	iMaxId integer;
	iSeqId integer;
begin
	execute 'set session_replication_role = replica';

	select max(id) into iRplId from osmapi_tables.rpl_journal_shadow;
	if iRplId is null then
		iRplId := 0;
	end if;
	insert into osmapi_tables.rpl_journal_shadow ( select * from osmapi_fdw.rpl_journal where id > iRplId );
	
	for recRpl in select * from osmapi_tables.rpl_journal_shadow where copied is null order by id
	loop
		if recRpl.opcode = 'I' then
			if recRpl.table_name = 'user_profiles' then
				insert into user_profiles select * from osmapi_fdw.user_profiles where id = recRpl.row_id;
				-- for privacy we don't want to have any personal data in depth, hence:
				update user_profiles set user_name = id, password = null, forename = null, surname = null, phone=null where id = recRpl.row_id;
			elsif recRpl.table_name = 'user_tracks' then
				insert into osmapi_tables.user_tracks(
					track_id,
					file_ref,
					upload_state,
					filetype,
					compression,
					containertrack,
					vesselconfigid,
					license,
					gauge_name,
					gauge,
					height_ref,
					comment,
					watertype,
					uploaddate,
					bbox,
					clusteruuid,
					clusterseq,
					upr_id,
					num_points,
					is_container)
				select
					track_id,
					file_ref,
					upload_state,
					filetype,
					compression,
					containertrack,
					vesselconfigid,
					license,
					gauge_name,
					gauge,
					height_ref,
					comment,
					watertype,
					uploaddate,
					bbox,
					clusteruuid,
					clusterseq,
					upr_id,
					num_points,
					is_container
				from 
					osmapi_fdw.user_tracks where track_id = recRpl.row_id;
--			update user_tracks set user_name = usr_id where track_id = recRpl.row_id;
			elsif recRpl.table_name = 'vesselconfiguration' then
				insert into osmapi_tables.vesselconfiguration(
					id,
					name,
					description,
					mmsi,
					manufacturer,
					model,
					loa,
					breadth,
					draft,
					height,
					displacement,
					maximumspeed,
					type,
					upr_id)
				select
					id,
					name,
					description,
					mmsi,
					manufacturer,
					model,
					loa,
					breadth,
					draft,
					height,
					displacement,
					maximumspeed,
					type,
					upr_id
				from 
					osmapi_fdw.vesselconfiguration where id = recRpl.row_id;
--				update vesselconfiguration set user_name = usr_id where id = iRowId;
			elsif recRpl.table_name = 'depthsensor' then
				insert into osmapi_tables.depthsensor select * from osmapi_fdw.depthsensor where id = recRpl.row_id;
			elsif recRpl.table_name = 'sbassensor' then
				insert into osmapi_tables.sbassensor select * from osmapi_fdw.sbassensor where id = recRpl.row_id;
			end if;
		elsif recRpl.opcode = 'U' then
			if recRpl.table_name = 'user_tracks' then
				select * into recUtr from osmapi_fdw.user_tracks where track_id = recRpl.row_id;
				update osmapi_tables.user_tracks set
					file_ref = recUtr.file_ref,
					upload_state = recUtr.upload_state,
					filetype = recUtr.filetype,
					compression = recUtr.compression,
					containertrack = recUtr.containertrack,
					vesselconfigid = recUtr.vesselconfigid,
					license = recUtr.license,
					gauge_name = recUtr.gauge_name,
					gauge = recUtr.gauge,
					height_ref = recUtr.height_ref,
					comment = recUtr.comment,
					watertype = recUtr.watertype,
					uploaddate = recUtr.uploaddate,
					bbox = recUtr.bbox,
					clusteruuid = recUtr.clusteruuid,
					clusterseq = recUtr.clusterseq,
					upr_id = recUtr.upr_id,
					num_points = recUtr.num_points,
					is_container = recUtr.is_container
				where track_id = recRpl.row_id;
			end if;
		end if;
		update osmapi_tables.rpl_journal_shadow set copied = now() where id = recRpl.id;
		iRows := iRows+1;
	end loop;
	
	perform adjustsequence( 'osmapi_tables.user_profiles_id_seq', ( select max( id ) from osmapi_tables.user_profiles ) );
	perform adjustsequence( 'osmapi_tables.user_tracks_track_id_seq', ( select max( track_id ) from osmapi_tables.user_tracks ) );
	perform adjustsequence( 'osmapi_tables.seq_tif', ( select max( id ) from osmapi_tables.track_info ) );
	perform adjustsequence( 'osmapi_tables.vesselconfiguration_id_seq', ( select max( id ) from osmapi_tables.vesselconfiguration ) );
	perform adjustsequence( 'osmapi_tables.depthsensor_id_seq', ( select max( id ) from osmapi_tables.depthsensor ) );
	perform adjustsequence( 'osmapi_tables.sbassensor_id_seq', ( select max( id ) from osmapi_tables.sbassensor ) );
	
	execute 'set session_replication_role = origin';
	
	return iRows;
end;
$$;


ALTER FUNCTION osmapi_tables.pullfromosmapi() OWNER TO postgres;

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
-- Name: createmergetables(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.createmergetables() RETURNS void
    LANGUAGE plpgsql
    AS $$
declare
	iZoom integer;
begin
	for iZoom in 1..22 loop
		execute 'drop table if exists ' || 'trackpoints_raw_merge_' || iZoom;
 		execute 'create table trackpoints_raw_merge_' || iZoom || '( gid bigint default nextval( ''seq_tpr'' ), dbs double precision, the_geom geometry, lat double precision, lon double precision, num_points bigint /*, call_id bigint, gids varchar*/ )';
		execute 'create index trackpoints_raw_merge_' || iZoom || '_geom on trackpoints_raw_merge_' || iZoom || ' using gist (the_geom )';
		execute 'alter table trackpoints_raw_merge_' || iZoom || ' add constraint trackpoints_raw_merge_' || iZoom || '_pk primary key( gid )';
	end loop;
end;
$$;


ALTER FUNCTION public.createmergetables() OWNER TO postgres;

--
-- Name: crosses(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.crosses(public.geometry, public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.3', 'crosses';


ALTER FUNCTION public.crosses(public.geometry, public.geometry) OWNER TO postgres;

--
-- Name: deleterenderdata(bigint); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.deleterenderdata(itrackid bigint) RETURNS void
    LANGUAGE plpgsql
    AS $$
declare
 iTab integer;
begin
 for iTab in 1..22
 loop
 execute 'delete from trackpoints_raw_render_' || iTab || ' where track_id = ' || iTrackId;
 end loop;
end;
$$;


ALTER FUNCTION public.deleterenderdata(itrackid bigint) OWNER TO postgres;

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
-- Name: dofillrawrendertables(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.dofillrawrendertables() RETURNS integer
    LANGUAGE plpgsql
    AS $$
declare 
	iSum1 bigint;
	iRet bigint := 0;
	recTrack bigint;
	dtEnd timestamp := clock_timestamp() + interval '1 Minute';
	iCount integer;
begin
	select count(*) into iCount from pg_stat_activity where query = 'select DoFillRawRenderTables()';
	
	if iCount >= 2 then
		return 0;
	end if;

	/* not needed here, done via trigger
	update osmapi_tables.user_tracks x set num_points = ( select count(*) from trackpoints_raw_filter_16 where datasetid = x.track_id ) 
		where 
			upload_state = 6 
			and num_points is null
			and not exists( select 1 from osmapi_tables.user_tracks y where y.containertrack = x.track_id );
	*/
	
	raise notice 'processing single tracks...';	
		
	for recTrack in ( 
			select track_id from osmapi_tables.user_tracks 
			where upload_state = 6 /* and containertrack is null */
			and not exists( select 1 from osmapi_tables.user_tracks sub where sub.containertrack = user_tracks.track_id )
			and not exists (select 1 from trackpoints_raw_render_1 where track_id = user_tracks.track_id ) 
			and not exists (select 1 from track_info where tra_id = user_tracks.track_id ) 
			and num_points > 0 ) order by track_id desc
			/*
			and exists (select 1 from trackpoints_raw_filter_16 where datasetid = user_tracks.track_id ) 
			limit 100 ) */
	loop
		exit when clock_timestamp() > dtEnd;
		raise notice 'processing track %', recTrack;
		select sum( count_points ) into iSum1 from fillrendertables( recTrack, 1, 22 );
		iRet := iRet + iSum1;
	end loop;

/*	
	raise notice 'processing multi tracks...';	

	for recTrack in ( 
			select track_id from osmapi_tables.user_tracks utr1
			where upload_state = 6 and is_container and num_points > 0 order by track_id desc )
--			and exists( select 1 from osmapi_tables.user_tracks sub where sub.containertrack = utr1.track_id )
--			and not exists (select 1 from trackpoints_raw_render_1 where track_id in( select track_id from osmapi_tables.user_tracks sub 
--				where containertrack = utr1.track_id ) ) 
--			and not exists (select 1 from track_info where tra_id in( select track_id from osmapi_tables.user_tracks sub 
--				where containertrack = utr1.track_id ) ) 
			-- and 0< ( select sum(num_points) from osmapi_tables.user_tracks sub2 where containertrack = utr1.track_id ) 
--			limit 100 )
	loop
		exit when clock_timestamp() > dtEnd;
		if not exists (select 1 from trackpoints_raw_render_1 where track_id in( select track_id from osmapi_tables.user_tracks sub 
				where containertrack = recTrack ) ) 
		then
			if not exists (select 1 from track_info where tra_id in( select track_id from osmapi_tables.user_tracks sub 
					where containertrack = recTrack or track_id = recTrack ) ) 
			then
				raise notice 'processing track %', recTrack;
				select sum( count_points ) into iSum1 from fillrendertables( recTrack, 1, 22 );
				raise notice 'Track % % points', recTrack, iSum1;
				iRet := iRet + iSum1;
			end if;
		end if;
	end loop;
*/
	return iRet;
			
end;
$$;


ALTER FUNCTION public.dofillrawrendertables() OWNER TO postgres;

--
-- Name: domergerun(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.domergerun() RETURNS void
    LANGUAGE plpgsql
    AS $$
declare
	iRunId bigint;
	dStart timestamp := clock_timestamp();
	iTrackId bigint;
	iPoints bigint;
	iTracks integer := 0;
	dStop timestamp := clock_timestamp() + interval '10 minutes';
begin
	insert into mergerun( started ) values( dStart ) returning id into iRunId;
	for iTrackId, iPoints in ( select track_id, num_points from osmapi_tables.user_tracks where ( not is_container ) and num_points > 0 and not exists	
		( select 1 from track_mergerun where tra_id = osmapi_tables.user_tracks.track_id ) order by track_id )
	loop
		iTracks := iTracks + 1;
		raise notice 'processing track % with % points (total % tracks)', iTrackId, iPoints, iTracks;
		perform MergeTrack( iTrackId );
		insert into track_mergerun( tra_id, mer_id ) values( iTrackId, iRunId );
		exit when clock_timestamp() > dStop;
	end loop;
	
	update mergerun set finished = clock_timestamp() where id = iRunId;
	
end;
$$;


ALTER FUNCTION public.domergerun() OWNER TO postgres;

--
-- Name: domergetrackpoints(integer, integer, double precision, double precision, double precision, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.domergetrackpoints(ilevel integer, imaxlevel integer, dleft double precision, dtop double precision, dright double precision, dbottom double precision) RETURNS void
    LANGUAGE plpgsql
    AS $$
declare
	dDegsPerBox double precision;
	dActLeft double precision;
	dActRight double precision;
	dActTop double precision;
	dActBottom double precision;
	dActTopExt double precision;
	dActBottomExt double precision;
	iDoLevel integer := iLevel;
begin
	if iDoLevel is null then
		iDoLevel := 1;
		dDegsPerBox := 4.5 / ( 2 ^ ( iDoLevel-1 ) );
		while dLeft + 0.5 * dDegsPerBox > dRight and iDoLevel < iMaxLevel
		loop
			iDoLevel := iDoLevel + 1;
			dDegsPerBox := 4.5 / ( 2 ^ ( iDoLevel-1 ) );
		end loop;
		raise notice 'calculated level % right %', iDoLevel, dLeft + dDegsPerBox; 
	end if;

	dDegsPerBox := 4.5 / ( 2 ^ ( iDoLevel-1 ) );
	dActLeft := dLeft;
	dActBottom := dBottom;
	
	while dActBottom < dTop 
	loop
		dActBottomExt := ExtLat( dActBottom );
		dActTopExt := dActBottomExt + dDegsPerBox;
		dActTop := ExtLatToLat( dActTopExt );
		raise notice 'act top: % act bottom: %', dActTop, dActBottom;
		dActLeft := dLeft;
		while dActLeft < dRight
		loop
			dActRight := dActLeft + dDegsPerBox;
			raise notice 'act left: % act right: %', dActLeft, dActRight;
			perform MergeTrackpoints( iDoLevel, iMaxLevel, dActLeft, dActTop, dActRight, dActBottom );
			dActLeft := dActRight;
		end loop;
		dActBottom := dActTop;
	end loop;
end;
$$;


ALTER FUNCTION public.domergetrackpoints(ilevel integer, imaxlevel integer, dleft double precision, dtop double precision, dright double precision, dbottom double precision) OWNER TO postgres;

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
-- Name: extlat(double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.extlat(dlat double precision) RETURNS double precision
    LANGUAGE plpgsql
    AS $$
declare
	dExtLat double precision;
begin
	dExtLat = 10800 / pi() * ln( tand( ( 45 + dLat / 2 ) ) ) / 60;
	return dExtLat;
end;
$$;


ALTER FUNCTION public.extlat(dlat double precision) OWNER TO postgres;

--
-- Name: extlattolat(double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.extlattolat(dextlat double precision) RETURNS double precision
    LANGUAGE plpgsql
    AS $$
declare
	dLat double precision;
begin
	dLat := atan( sinhd( dExtLat ) ) * 180.0 / PI();
	return dLat;
end;
$$;


ALTER FUNCTION public.extlattolat(dextlat double precision) OWNER TO postgres;

--
-- Name: fillrendertables(bigint, integer, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.fillrendertables(itrackid bigint, istartzoom integer DEFAULT 1, imaxzoom integer DEFAULT 22) RETURNS TABLE(zoom integer, count_points integer)
    LANGUAGE plpgsql
    AS $_$
declare
	dLat double precision;
	dLon double precision;
	dDbs  double precision;
	iGid bigint;
	iZoom integer;
	iPoints integer := 0;
	iCounts integer[22];
	iTotalCounts integer[22];
	dLats double precision[22];
	dLons double precision[22];
	dDepths double precision[22];
	dLastLats double precision[22];
	dLastLons double precision[22];
	dExtLat double precision;
	dMinDistances double precision[22];
	dOutLat double precision;
	dOutLon double precision;
	dOutDepth double precision;
	dDist double precision;
	iLastGid bigint;
	iLastTrackId bigint;
	iInsTrackId bigint;
begin
	raise notice 'FillRenderTables: processing track %', iTrackId;
	-- ensure at least 5 pixels of distance between 2 points
	dMinDistances[1] = 5.0 * 360 / 256;
	
	for iZoom in 2..iMaxZoom loop
		dMinDistances[ iZoom ] := dMinDistances[ iZoom-1 ] / 2;
	end loop;

	for iZoom in iStartZoom..iMaxZoom loop	
		iTotalCounts[iZoom] := 0;
	/*
		begin
		execute 'deallocate insrender_' || iZoom; 
		exception when others then null; end;
		execute 'prepare insrender_' || iZoom || '( bigint, bigint, double precision, double precision, double precision ) as ' ||
			'insert into trackpoints_raw_render_' || iZoom || ' values( $1, $2, $3, ST_SetSRID(ST_MakePoint($4, $5), 4326), $5, $4 )'; 
		execute 'delete from trackpoints_raw_render_' || iZoom || ' where track_id = ' || iTrackId;
	*/
	end loop;
	
	delete from trackpoints_raw_render_1 where track_id = iTrackId;
	delete from trackpoints_raw_render_2 where track_id = iTrackId;
	delete from trackpoints_raw_render_3 where track_id = iTrackId;
	delete from trackpoints_raw_render_4 where track_id = iTrackId;
	delete from trackpoints_raw_render_5 where track_id = iTrackId;
	delete from trackpoints_raw_render_6 where track_id = iTrackId;
	delete from trackpoints_raw_render_7 where track_id = iTrackId;
	delete from trackpoints_raw_render_8 where track_id = iTrackId;
	delete from trackpoints_raw_render_9 where track_id = iTrackId;
	delete from trackpoints_raw_render_10 where track_id = iTrackId;
	delete from trackpoints_raw_render_11 where track_id = iTrackId;
	delete from trackpoints_raw_render_12 where track_id = iTrackId;
	delete from trackpoints_raw_render_13 where track_id = iTrackId;
	delete from trackpoints_raw_render_14 where track_id = iTrackId;
	delete from trackpoints_raw_render_15 where track_id = iTrackId;
	delete from trackpoints_raw_render_16 where track_id = iTrackId;
	delete from trackpoints_raw_render_17 where track_id = iTrackId;
	delete from trackpoints_raw_render_18 where track_id = iTrackId;
	delete from trackpoints_raw_render_19 where track_id = iTrackId;
	delete from trackpoints_raw_render_20 where track_id = iTrackId;
	delete from trackpoints_raw_render_21 where track_id = iTrackId;
	delete from trackpoints_raw_render_22 where track_id = iTrackId;
	
	for dLat, dLon, dDbs, iGid, iInsTrackId in 
		          select lat::double precision, lon::double precision, dbs::double precision, gid::bigint, datasetid from trackpoints_raw_filter_16 where datasetid = iTrackId
--		union all select lat::double precision, lon::double precision, dbs::double precision, gid::bigint, datasetid from trackpoints_raw_filter_16 where datasetid in
--			( select track_id from user_tracks where containertrack = iTrackId )
		union all select 0.0, 200.0, 0::numeric(8,2), 999999999999, 0  order by gid
	loop
		if iPoints = 0 then
			raise notice 'FillRenderTables: starting processing...';
		end if;
		dExtLat = 10800 / pi() * ln( tan( ( 45 + dLat / 2 ) / 360 * pi() * 2 ) ) / 60;
		for iZoom in iStartZoom..iMaxZoom loop
			if dLastLats[iZoom] is not null then
				dDist = sqrt( ( dLastLats[iZoom] - dExtLat ) ^ 2 + ( dLastLons[iZoom] - dLon ) ^ 2 );
				if dDist > dMinDistances[iZoom] then -- need new point
					dOutLat := dLats[iZoom] / iCounts[iZoom];
					dOutLon := dLons[iZoom] / iCounts[iZoom];
					dOutDepth := dDepths[iZoom] / iCounts[iZoom];
					
					if iZoom = 1 then	
						insert into trackpoints_raw_render_1 values( iLastGid, iLastTrackId, dOutDepth, ST_SetSRID(ST_MakePoint(dOutLon, dOutLat), 4326) );
					elsif iZoom = 2 then	
						insert into trackpoints_raw_render_2 values( iLastGid, iLastTrackId, dOutDepth, ST_SetSRID(ST_MakePoint(dOutLon, dOutLat), 4326) );
					elsif iZoom = 3 then	
						insert into trackpoints_raw_render_3 values( iLastGid, iLastTrackId, dOutDepth, ST_SetSRID(ST_MakePoint(dOutLon, dOutLat), 4326) );
					elsif iZoom = 4 then	
						insert into trackpoints_raw_render_4 values( iLastGid, iLastTrackId, dOutDepth, ST_SetSRID(ST_MakePoint(dOutLon, dOutLat), 4326) );
					elsif iZoom = 5 then	
						insert into trackpoints_raw_render_5 values( iLastGid, iLastTrackId, dOutDepth, ST_SetSRID(ST_MakePoint(dOutLon, dOutLat), 4326) );
					elsif iZoom = 6 then	
						insert into trackpoints_raw_render_6 values( iLastGid, iLastTrackId, dOutDepth, ST_SetSRID(ST_MakePoint(dOutLon, dOutLat), 4326) );
					elsif iZoom = 7 then	
						insert into trackpoints_raw_render_7 values( iLastGid, iLastTrackId, dOutDepth, ST_SetSRID(ST_MakePoint(dOutLon, dOutLat), 4326) );
					elsif iZoom = 8 then	
						insert into trackpoints_raw_render_8 values( iLastGid, iLastTrackId, dOutDepth, ST_SetSRID(ST_MakePoint(dOutLon, dOutLat), 4326) );
					elsif iZoom = 9 then	
						insert into trackpoints_raw_render_9 values( iLastGid, iLastTrackId, dOutDepth, ST_SetSRID(ST_MakePoint(dOutLon, dOutLat), 4326) );
					elsif iZoom = 10 then	
						insert into trackpoints_raw_render_10 values( iLastGid, iLastTrackId, dOutDepth, ST_SetSRID(ST_MakePoint(dOutLon, dOutLat), 4326) );
					elsif iZoom = 11 then	
						insert into trackpoints_raw_render_11 values( iLastGid, iLastTrackId, dOutDepth, ST_SetSRID(ST_MakePoint(dOutLon, dOutLat), 4326) );
					elsif iZoom = 12 then	
						insert into trackpoints_raw_render_12 values( iLastGid, iLastTrackId, dOutDepth, ST_SetSRID(ST_MakePoint(dOutLon, dOutLat), 4326) );
					elsif iZoom = 13 then	
						insert into trackpoints_raw_render_13 values( iLastGid, iLastTrackId, dOutDepth, ST_SetSRID(ST_MakePoint(dOutLon, dOutLat), 4326) );
					elsif iZoom = 14 then	
						insert into trackpoints_raw_render_14 values( iLastGid, iLastTrackId, dOutDepth, ST_SetSRID(ST_MakePoint(dOutLon, dOutLat), 4326) );
					elsif iZoom = 15 then	
						insert into trackpoints_raw_render_15 values( iLastGid, iLastTrackId, dOutDepth, ST_SetSRID(ST_MakePoint(dOutLon, dOutLat), 4326) );
					elsif iZoom = 16 then	
						insert into trackpoints_raw_render_16 values( iLastGid, iLastTrackId, dOutDepth, ST_SetSRID(ST_MakePoint(dOutLon, dOutLat), 4326) );
					elsif iZoom = 17 then	
						insert into trackpoints_raw_render_17 values( iLastGid, iLastTrackId, dOutDepth, ST_SetSRID(ST_MakePoint(dOutLon, dOutLat), 4326) );
					elsif iZoom = 18 then	
						insert into trackpoints_raw_render_18 values( iLastGid, iLastTrackId, dOutDepth, ST_SetSRID(ST_MakePoint(dOutLon, dOutLat), 4326) );
					elsif iZoom = 19 then	
						insert into trackpoints_raw_render_19 values( iLastGid, iLastTrackId, dOutDepth, ST_SetSRID(ST_MakePoint(dOutLon, dOutLat), 4326) );
					elsif iZoom = 20 then	
						insert into trackpoints_raw_render_20 values( iLastGid, iLastTrackId, dOutDepth, ST_SetSRID(ST_MakePoint(dOutLon, dOutLat), 4326) );
					elsif iZoom = 21 then	
						insert into trackpoints_raw_render_21 values( iLastGid, iLastTrackId, dOutDepth, ST_SetSRID(ST_MakePoint(dOutLon, dOutLat), 4326) );
					elsif iZoom = 22 then	
						insert into trackpoints_raw_render_22 values( iLastGid, iLastTrackId, dOutDepth, ST_SetSRID(ST_MakePoint(dOutLon, dOutLat), 4326) );
					end if;
					
--					execute 'execute insrender_' || iZoom || '( $1, $2, $3, $4 )' using iTrackId, dDbs, dLon, dLat;
--					execute 'execute insrender_' || iZoom || '( ' || iLastGid || ',' || iLastTrackId || ',' || dOutDepth || ',' || dOutLon || ',' || dOutLat || ')';
					
					dLastLats[ iZoom ] := null;
					iTotalCounts[ iZoom ] := iTotalCounts[ iZoom ] + 1;
				else
					dLats[ iZoom ] := dLats[ iZoom ] + dLat;
					dLons[ iZoom ] := dLons[ iZoom ] + dLon;
					dDepths[ iZoom ] := dDepths[ iZoom ] + dDbs;
					iCounts[ iZoom ] := iCounts[ iZoom ] + 1;
					-- dLastLats[ iZoom ] := dExtLat;
				end if;
			end if;
			if dLastLats[iZoom] is null then
				dLastLats[ iZoom ] := dExtLat;
				dLastLons[ iZoom ] := dLon;
				dLats[ iZoom ] := dLat;
				dLons[ iZoom ] := dLon;
				dDepths[ iZoom ] := dDbs;
				iCounts[ iZoom ] := 1;
			end if;
		end loop;
		iPoints := iPoints+1;
		iLastGid := iGid;
		iLastTrackId := iInsTrackId;
		if iPoints % 10000 = 0 then
			raise notice 'processed % points', iPoints;
		end if;
	end loop;
	
	for iZoom in 1..iMaxZoom loop	
		zoom := iZoom;
		count_points := iTotalCounts[ iZoom ];
		return next;
	end loop;
	
end;
$_$;


ALTER FUNCTION public.fillrendertables(itrackid bigint, istartzoom integer, imaxzoom integer) OWNER TO postgres;

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
-- Name: fub_utr(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.fub_utr() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
declare
	iCount bigint;
	iCount6 bigint;
	geomBBox geometry;
	iPointsSub bigint;
	iCountSubNotOk bigint;
begin
	-- if processing is done, update track with point count;
	if old.upload_state <> 6 and new.upload_state = 6 then
		if exists( select 1 from user_tracks where containertrack = new.track_id ) then
			new.is_container = true;
		else
			new.is_container = false;
			select count(*) into iCount from trackpoints_raw_filter_16 where datasetid = new.track_id;
			if iCount = 0 then
				select count(*) into iCount from trackpoints_raw_temp_16 where datasetid = new.track_id;
				geomBBox := ST_SetSRID( ( select ST_Extent( the_geom ) from trackpoints_raw_temp_16 
					where datasetid = new.track_id ), 4326 );
			else
				geomBBox := ST_SetSRID( ( select ST_Extent( the_geom ) from trackpoints_raw_filter_16 
					where datasetid = new.track_id ), 4326 );
			end if;
			new.num_points = iCount;
			if iCount = 0 then
				new.upload_state = 7;
			end if;
			if geometrytype( geomBBox ) = 'POLYGON' then
				new.bbox = geomBBox;
			end if;
		end if;
	end if;
	
	if new.containertrack is not null and new.upload_state is distinct from old.upload_state then
		update osmapi_tables.user_tracks set is_container = true where track_id = new.containertrack;
		if new.upload_state in ( 6, 7 ) then -- mark track as processed if and only if all sub-tracks have been processed.
			geomBBox := ST_SetSRID( ( select ST_Extent( bbox ) from 
						( select bbox from user_tracks
							where containertrack = new.containertrack and bbox is not null and track_id <> new.track_id  
							union all
							select new.bbox) x )
							, 4326 );
			select count(*) into iCountSubNotOk from osmapi_tables.user_tracks where containertrack = new.containertrack 
				and track_id <> new.track_id and upload_state not in ( 6, 7 );
			if new.upload_state = 6 then	
				iCount6 := 1;
			else
				select count(*) into iCount6 from user_tracks where containertrack = new.containertrack and upload_state = 6 and track_id <> new.track_id;
			end if;
			if iCount6 > 0 or new.upload_state = 6 then
				update osmapi_tables.user_tracks set upload_state = 6,
					num_points = 
						coalesce
						(
							( 
								select sum( coalesce( num_points, 0 ) ) from osmapi_tables.user_tracks x
								where containertrack = new.containertrack and upload_state = 6 and track_id <> new.track_id 
							) , 
							0 
						) + 
						coalesce( new.num_points, 0 ),
						bbox = geomBBox
				where track_id = new.containertrack;
			else
				update osmapi_tables.user_tracks set upload_state = 7, bbox=null where track_id = new.containertrack;
			end if;
		end if;
		if iCountSubNotOk = 0 then
			update osmapi_tables.user_tracks set upload_state = 6 where track_id = new.containertrack;
		elsif new.upload_state = 14 then
			update osmapi_tables.user_tracks set upload_state = 14 where track_id = new.containertrack;
		elsif new.upload_state = 3 then
			update osmapi_tables.user_tracks set upload_state = 3 where track_id = new.containertrack;
		end if;
	end if;
	return new;
end;
$$;


ALTER FUNCTION public.fub_utr() OWNER TO postgres;

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
-- Name: mergetrack(bigint); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.mergetrack(itrackid bigint) RETURNS void
    LANGUAGE plpgsql
    AS $_$
declare
	recPoints trackpoints_raw_merge_22;
	iLevel integer := 22;
	dDegsPerBox double precision;
	dTop double precision;
	dBottom double precision;
	dLeft double precision;
	dRight double precision;
	geomBox geometry;
	dExtLat double precision;
	tpInsert merged_trackpoint;
	iCountStart integer;
	iTotal integer := 0;
	iRounds integer := 0;
	dBottomExt double precision;
	recMerged trackpoints_raw_merge_22;
	iTrpId bigint;
	dTempLat double precision;
	dTempLon double precision;
	dTempDbs double precision;
	iTempCnt integer;
	iUpdated integer := 0;
	iInserted integer := 0;
begin
	execute 'drop table if exists trackpoints_raw_merge_23';
	execute 'create temporary table if not exists trackpoints_raw_merge_23( like trackpoints_raw_merge_22 ) on commit drop';
	insert into trackpoints_raw_merge_23 select gid, dbs, the_geom, lat, lon, 1 from trackpoints_raw_filter_16 where datasetid = iTrackid;
	execute 'create index trackpoints_raw_merge_23_geom on trackpoints_raw_merge_23 using gist( the_geom )';
	execute 'create index trackpoints_raw_merge_23_gid on trackpoints_raw_merge_23( gid )';
	execute 'analyze trackpoints_raw_merge_23';
	
	raise notice 'tmp table created';

-- this view was created to do all the dynamic stuff with a similiar name.

--	execute 'drop view if exists trackpoints_raw_merge_23';
--	execute 'create view trackpoints_raw_merge_23 as select gid, dbs, the_geom, lat, lon, 1 as num_points from trackpoints_raw_filter_16 where datasetid = ' || iTrackId;

	execute 'create temporary table if not exists trp_to_merge( level integer not null, trp_id bigint not null, ' ||
			' constraint ttm_pk primary key( level, trp_id ) ) on commit drop';

	execute 'truncate table trp_to_merge';
	
	insert into trp_to_merge select 23, gid from trackpoints_raw_merge_23;
	
	for iLevel in reverse 22..1
	loop
		execute 'analyze trp_to_merge';
		iRounds := 0;
		select reltuples into strict iCountStart from pg_class where relkind = 'r' and relname = 'trp_to_merge';
		
		dDegsPerBox := 4.5 / ( 2 ^ ( iLevel-1 ) );
		
		loop
			select trp_id from trp_to_merge into iTrpId where level = iLevel+1 order by trp_id limit 1;  -- ordering just for debug purposes, remove later
			exit when iTrpId is null;
			execute 'select * from trackpoints_raw_merge_' || iLevel+1 || ' where gid = $1' into strict recPoints using iTrpId;
			
			dExtLat := ExtLat( recPoints.lat );
			
			-- first, decide which 'box' the point is in
			dLeft := floor( recPoints.lon / dDegsPerbox ) * dDegsPerBox;
			dRight := dLeft + dDegsPerBox;
			dBottomExt := floor( dExtLat / dDegsPerbox ) * dDegsPerBox;
			dBottom := ExtLatToLat( dBottomExt );
			dTop := ExtLatToLat( dBottomExt + dDegsPerBox );
			
			if dLeft > recPoints.lon then
				dLeft = recPoints.lon;
			end if;
			if dRight < recPoints.lon then
				dRight = recPoints.lon;
			end if;
			if dBottom > recPoints.lat then
				dBottom = recPoints.lat;
			end if;
			if dTop < recPoints.lat then
				dTop = recPoints.lat;
			end if;
			
			if iRounds < 1 then
				raise notice 'lat % lon % left % right % bottom % top % width % height %', recPoints.lat, recPoints.lon, dLeft, dRight, dBottom, dTop, dRight-dLeft, dTop-dBottom;
			end if;
			
			geomBox := ST_SetSRID(
				ST_MakeBox2D(
					ST_Point( dLeft - 1e-6, dBottom - 1e-6  ),
					ST_Point( dRight + 1e-6 , dTop + 1e-6 )
				),
				4326 );

			-- then process all points in this box, deleting them from the temp table aftwrewards
			tpInsert.lat := 0;
			tpInsert.lon := 0;
			tpInsert.depth := 0;
			tpInsert.num_points := 0;
			
			for dTempLat, dTempLon, dTempDbs, iTrpId, iTempCnt in execute 'select lat, lon, dbs, gid, num_points from trackpoints_raw_merge_' || iLevel+1 ||
				' where the_geom && $1 and lon >= $2 and lon <= $3 and lat >= $4 and lat <= $5' 
				using geomBox, dLeft, dRight, dBottom, dTop
			loop
				tpInsert.lat := tpInsert.lat + dTempLat * iTempCnt;
				tpInsert.lon := tpInsert.lon + dTempLon * iTempCnt;
				tpInsert.depth := tpInsert.depth + dTempDbs * iTempCnt;
				tpInsert.num_points := tpInsert.num_points + iTempCnt;
				delete from trp_to_merge where level = iLevel+1 and trp_id = iTrpId;
			end loop;
			if tpInsert.num_points = 0 then
				raise notice 'gid % lat % lon % left % right % bottom % top % width % height %', recPoints.gid, recPoints.lat, recPoints.lon, dLeft, dRight, dBottom, dTop, dRight-dLeft, dTop-dBottom;
			end if;
			tpInsert.lat := tpInsert.lat / tpInsert.num_points;
			tpInsert.lon := tpInsert.lon / tpInsert.num_points;
			tpInsert.depth := tpInsert.depth / tpInsert.num_points;
			
			iTotal := iTotal+tpInsert.num_points;
			iRounds := iRounds + 1;
			
			-- do we already have a point in this box?
			execute 'select * from trackpoints_raw_merge_' || iLevel ||
				' where the_geom && $1 and lon >= $2 and lon <= $3 and lat >= $4 and lat <= $5'
				into recMerged using geomBox, dLeft, dRight, dBottom, dTop;
				
			--select * into recMerged from trackpoints_raw_merge_22 where the_geom && geomBox and lon >= dLeft and lon < dRight and lat >= dBottom and lat <= dTop;

			if recMerged.gid is not null then -- data present in this box, update it
				if iLevel = 22 then
					tpInsert.lat := ( recMerged.lat * recMerged.num_points + tpInsert.lat * tpInsert.num_points ) / ( recMerged.num_points + tpInsert.num_points );
					tpInsert.lon := ( recMerged.lon * recMerged.num_points + tpInsert.lon * tpInsert.num_points ) / ( recMerged.num_points + tpInsert.num_points );
					tpInsert.depth := ( recMerged.dbs * recMerged.num_points + tpInsert.depth * tpInsert.num_points ) / ( recMerged.num_points + tpInsert.num_points );
					tpInsert.num_points := recMerged.num_points + tpInsert.num_points;
				end if;
				execute 'update trackpoints_raw_merge_' || iLevel ||' set lat = $1, lon = $2, dbs = $3, num_points = $4, the_geom = $5 where gid = $6'
					using tpInsert.lat, tpInsert.lon, tpInsert.depth, tpInsert.num_points,
						ST_SetSRID( ST_Point( tpInsert.lon, tpInsert.lat ), 4326 ), recMerged.gid;
				if not exists ( select 1 from trp_to_merge where level = iLevel and trp_id = recMerged.gid ) then
					insert into trp_to_merge values( iLevel, recMerged.gid );
				end if;
				iUpdated := iUpdated + 1;
			else -- no, insert
				execute 'insert into trackpoints_raw_merge_' || iLevel || '( lat, lon, dbs, num_points, the_geom ) values( $1, $2, $3, $4, $5 ) returning gid'
				using tpInsert.lat, tpInsert.lon, tpInsert.depth, tpInsert.num_points,
					ST_SetSRID( ST_Point( tpInsert.lon, tpInsert.lat ), 4326 )
				into iTrpId;
				insert into trp_to_merge values( iLevel, iTrpId );
				iInserted := iInserted + 1;
			end if;
				
			--if iRounds%100 = 1 or tpInsert.num_points > 1 then
			--	raise notice 'round % points %', iRounds, tpInsert.num_points;
			--end if;
				
		end loop;
		raise notice 'count before % processed % updated % inserted %', iCountStart, iTotal, iUpdated, iInserted;
	end loop;
	
end;
$_$;


ALTER FUNCTION public.mergetrack(itrackid bigint) OWNER TO postgres;

--
-- Name: mergetrackpoints(integer, integer, double precision, double precision, double precision, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.mergetrackpoints(ilevel integer, imaxlevel integer, dleft double precision, dtop double precision, dright double precision, dbottom double precision) RETURNS public.merged_trackpoint
    LANGUAGE plpgsql
    AS $$
declare
	iCount integer;
	geomBox geometry;
	iSubX integer;
	iSubY integer;
	dSubLeft double precision;
	dSubRight double precision;
	dSubTop double precision;
	dSubBottom double precision;
	-- dSubTopExt double precision;
	-- dSubBottomExt double precision;
	dTopExt double precision := ExtLat( dTop );
	dBottomExt double precision := ExtLat( dBottom );
	dHalfWidth double precision := ( dRight - dLeft ) / 2.0;
	dHalfHeightExt double precision := ( dTopExt - dBottomExt ) / 2.0;
	bRecurse boolean := false;
	iCountR integer;
	tpRet merged_trackpoint;
	tp merged_trackpoint;
	geomPoint geometry;
	iCallId bigint;
	cGids varchar;
	iMinInsertLevel integer := iLevel;
	iMaxInsertLevel integer	:= iLevel;
begin
	iCallId := nextval( 'call_id_seq' );
	
	geomBox := ST_SetSRID(
		ST_MakeBox2D(
			ST_Point( dLeft, dBottom  ),
			ST_Point( dRight , dTop )
		),
		4326 );
	
	/* first, we select the count of points in this zoom level from the 
	render table to get a clue of how many trackpoints there are */
	/*	
	if iLevel = 1 then select count( * ) into iCount from trackpoints_raw_render_1 where the_geom && geomBox;
	elsif iLevel = 2 then select count( * ) into iCount from trackpoints_raw_render_2 where the_geom && geomBox;
	elsif iLevel = 3 then select count( * ) into iCount from trackpoints_raw_render_3 where the_geom && geomBox;
	elsif iLevel = 4 then select count( * ) into iCount from trackpoints_raw_render_4 where the_geom && geomBox;
	elsif iLevel = 5 then select count( * ) into iCount from trackpoints_raw_render_5 where the_geom && geomBox;
	elsif iLevel = 6 then select count( * ) into iCount from trackpoints_raw_render_6 where the_geom && geomBox;
	elsif iLevel = 7 then select count( * ) into iCount from trackpoints_raw_render_7 where the_geom && geomBox;
	elsif iLevel = 8 then select count( * ) into iCount from trackpoints_raw_render_8 where the_geom && geomBox;
	elsif iLevel = 9 then select count( * ) into iCount from trackpoints_raw_render_9 where the_geom && geomBox;
	elsif iLevel = 10 then select count( * ) into iCount from trackpoints_raw_render_10 where the_geom && geomBox;
	elsif iLevel = 11 then select count( * ) into iCount from trackpoints_raw_render_11 where the_geom && geomBox;
	elsif iLevel = 12 then select count( * ) into iCount from trackpoints_raw_render_12 where the_geom && geomBox;
	elsif iLevel = 13 then select count( * ) into iCount from trackpoints_raw_render_13 where the_geom && geomBox;
	elsif iLevel = 14 then select count( * ) into iCount from trackpoints_raw_render_14 where the_geom && geomBox;
	elsif iLevel = 15 then select count( * ) into iCount from trackpoints_raw_render_15 where the_geom && geomBox;
	elsif iLevel = 16 then select count( * ) into iCount from trackpoints_raw_render_16 where the_geom && geomBox;
	elsif iLevel = 17 then select count( * ) into iCount from trackpoints_raw_render_17 where the_geom && geomBox;
	elsif iLevel = 18 then select count( * ) into iCount from trackpoints_raw_render_18 where the_geom && geomBox;
	elsif iLevel = 19 then select count( * ) into iCount from trackpoints_raw_render_19 where the_geom && geomBox;
	elsif iLevel = 20 then select count( * ) into iCount from trackpoints_raw_render_20 where the_geom && geomBox;
	elsif iLevel = 21 then select count( * ) into iCount from trackpoints_raw_render_21 where the_geom && geomBox;
	elsif iLevel = 22 then select count( * ) into iCount from trackpoints_raw_render_22 where the_geom && geomBox;
	end if;
	*/
	
	select count( * ) into iCount from 
		( select * from trackpoints_raw_filter_16 where the_geom && geomBox 
		  and lon >= dLeft and lon < dRight and lat >= dBottom and lat < dTop limit 2 ) sub;
	
	iCountR := iCount;
	
	-- if any points are there, we recurse
	
	if iCount > 1 and iLevel < iMaxLevel then -- if more than 0 points in render table, definitely recurse
		bRecurse := true;
	end if;

	if iLevel <= 8 then
		raise notice 'MergeTrackpoints( iLevel %, iCallId %, iMaxLevel %, dLeft %, dTop %, dRight %, dBottom %, points r %, points t % )', iLevel, iCallId, iMaxLevel, dLeft, dTop, dRight, dBottom, iCountR, iCount;
	end if;
	
	-- for recursion, we split our square in 4
	if bRecurse then
		tpRet.lat := 0;
		tpRet.lon := 0;
		tpRet.depth := 0;
		tpRet.num_points := 0;
		dSubBottom := dBottom;
		for iSubY in 0..1
		loop
			dSubTop := case when iSubY = 0 then ExtLatToLat( dBottomExt + dHalfHeightExt ) else dTop end;
			for iSubX in 0..1
			loop
				dSubLeft := case when iSubX = 0 then dLeft else dLeft + dHalfWidth end;
				dSubRight := dSubLeft + dHalfWidth;
				tp := MergeTrackpoints( iLevel+1, iMaxLevel, dSubLeft, dSubTop, dSubRight, dSubBottom );
				if tp is not null then
					tpRet.lat := tpRet.lat + tp.lat * tp.num_points;
					tpRet.lon := tpRet.lon + tp.lon * tp.num_points;
					tpRet.depth := tpRet.depth + tp.depth * tp.num_points;
					tpRet.num_points := tpRet.num_points + tp.num_points;
				end if;
			end loop;
			dSubBottom := dSubTop;
		end loop;
		if tpRet.num_points > 0 then
			tpRet.lat := tpRet.lat / tpRet.num_points;
			tpRet.lon := tpRet.lon / tpRet.num_points;
			tpRet.depth := tpRet.depth / tpRet.num_points;
		else
			return null;
		end if;
	elsif iCount > 0 then
		-- as of postgres 9.6.6, GIST indexes procuce false positives, so we have to recheck.
		select avg( lat ), avg( lon ), avg( dbs ), count(*), array_agg( gid ) 
			into tpRet.lat, tpRet.lon, tpRet.depth, tpRet.num_points, cGids 
			from trackpoints_raw_filter_16 where the_geom && geomBox and
			lon >= dLeft and lon < dRight and lat >= dBottom and lat < dTop;
		iMaxInsertLevel := 22; -- no further processing in deeper levels, so propagate results
	else	
		return null;
	end if;
	
	-- if we do not recurse, we still have to insert our point into all finer zoom levels.
	if tpRet.num_points > 0 then
		geomPoint := ST_SetSRID( ST_Point( tpRet.lon, tpRet.lat  ), 4326 );
		if iMinInsertLevel <= 1 and iMaxInsertLevel >= 1 then insert into trackpoints_raw_merge_1( dbs, the_geom, lat, lon, num_points/* , call_id, gids */ ) values( tpRet.depth, geomPoint, tpRet.lat, tpRet.lon, tpRet.num_points/* , iCallId, cGids */ ); end if;
		if iMinInsertLevel <= 2 and iMaxInsertLevel >= 2 then insert into trackpoints_raw_merge_2( dbs, the_geom, lat, lon, num_points/* , call_id, gids */ ) values( tpRet.depth, geomPoint, tpRet.lat, tpRet.lon, tpRet.num_points/* , iCallId, cGids */ ); end if;
		if iMinInsertLevel <= 3 and iMaxInsertLevel >= 3 then insert into trackpoints_raw_merge_3( dbs, the_geom, lat, lon, num_points/* , call_id, gids */ ) values( tpRet.depth, geomPoint, tpRet.lat, tpRet.lon, tpRet.num_points/* , iCallId, cGids */ ); end if;
		if iMinInsertLevel <= 4 and iMaxInsertLevel >= 4 then insert into trackpoints_raw_merge_4( dbs, the_geom, lat, lon, num_points/* , call_id, gids */ ) values( tpRet.depth, geomPoint, tpRet.lat, tpRet.lon, tpRet.num_points/* , iCallId, cGids */ ); end if;
		if iMinInsertLevel <= 5 and iMaxInsertLevel >= 5 then insert into trackpoints_raw_merge_5( dbs, the_geom, lat, lon, num_points/* , call_id, gids */ ) values( tpRet.depth, geomPoint, tpRet.lat, tpRet.lon, tpRet.num_points/* , iCallId, cGids */ ); end if;
		if iMinInsertLevel <= 6 and iMaxInsertLevel >= 6 then insert into trackpoints_raw_merge_6( dbs, the_geom, lat, lon, num_points/* , call_id, gids */ ) values( tpRet.depth, geomPoint, tpRet.lat, tpRet.lon, tpRet.num_points/* , iCallId, cGids */ ); end if;
		if iMinInsertLevel <= 7 and iMaxInsertLevel >= 7 then insert into trackpoints_raw_merge_7( dbs, the_geom, lat, lon, num_points/* , call_id, gids */ ) values( tpRet.depth, geomPoint, tpRet.lat, tpRet.lon, tpRet.num_points/* , iCallId, cGids */ ); end if;
		if iMinInsertLevel <= 8 and iMaxInsertLevel >= 8 then insert into trackpoints_raw_merge_8( dbs, the_geom, lat, lon, num_points/* , call_id, gids */ ) values( tpRet.depth, geomPoint, tpRet.lat, tpRet.lon, tpRet.num_points/* , iCallId, cGids */ ); end if;
		if iMinInsertLevel <= 9 and iMaxInsertLevel >= 9 then insert into trackpoints_raw_merge_9( dbs, the_geom, lat, lon, num_points/* , call_id, gids */ ) values( tpRet.depth, geomPoint, tpRet.lat, tpRet.lon, tpRet.num_points/* , iCallId, cGids */ ); end if;
		if iMinInsertLevel <= 10 and iMaxInsertLevel >= 10 then insert into trackpoints_raw_merge_10( dbs, the_geom, lat, lon, num_points/* , call_id, gids */ ) values( tpRet.depth, geomPoint, tpRet.lat, tpRet.lon, tpRet.num_points/* , iCallId, cGids */ ); end if;
		if iMinInsertLevel <= 11 and iMaxInsertLevel >= 11 then insert into trackpoints_raw_merge_11( dbs, the_geom, lat, lon, num_points/* , call_id, gids */ ) values( tpRet.depth, geomPoint, tpRet.lat, tpRet.lon, tpRet.num_points/* , iCallId, cGids */ ); end if;
		if iMinInsertLevel <= 12 and iMaxInsertLevel >= 12 then insert into trackpoints_raw_merge_12( dbs, the_geom, lat, lon, num_points/* , call_id, gids */ ) values( tpRet.depth, geomPoint, tpRet.lat, tpRet.lon, tpRet.num_points/* , iCallId, cGids */ ); end if;
		if iMinInsertLevel <= 13 and iMaxInsertLevel >= 13 then insert into trackpoints_raw_merge_13( dbs, the_geom, lat, lon, num_points/* , call_id, gids */ ) values( tpRet.depth, geomPoint, tpRet.lat, tpRet.lon, tpRet.num_points/* , iCallId, cGids */ ); end if;
		if iMinInsertLevel <= 14 and iMaxInsertLevel >= 14 then insert into trackpoints_raw_merge_14( dbs, the_geom, lat, lon, num_points/* , call_id, gids */ ) values( tpRet.depth, geomPoint, tpRet.lat, tpRet.lon, tpRet.num_points/* , iCallId, cGids */ ); end if;
		if iMinInsertLevel <= 15 and iMaxInsertLevel >= 15 then insert into trackpoints_raw_merge_15( dbs, the_geom, lat, lon, num_points/* , call_id, gids */ ) values( tpRet.depth, geomPoint, tpRet.lat, tpRet.lon, tpRet.num_points/* , iCallId, cGids */ ); end if;
		if iMinInsertLevel <= 16 and iMaxInsertLevel >= 16 then insert into trackpoints_raw_merge_16( dbs, the_geom, lat, lon, num_points/* , call_id, gids */ ) values( tpRet.depth, geomPoint, tpRet.lat, tpRet.lon, tpRet.num_points/* , iCallId, cGids */ ); end if;
		if iMinInsertLevel <= 17 and iMaxInsertLevel >= 17 then insert into trackpoints_raw_merge_17( dbs, the_geom, lat, lon, num_points/* , call_id, gids */ ) values( tpRet.depth, geomPoint, tpRet.lat, tpRet.lon, tpRet.num_points/* , iCallId, cGids */ ); end if;
		if iMinInsertLevel <= 18 and iMaxInsertLevel >= 18 then insert into trackpoints_raw_merge_18( dbs, the_geom, lat, lon, num_points/* , call_id, gids */ ) values( tpRet.depth, geomPoint, tpRet.lat, tpRet.lon, tpRet.num_points/* , iCallId, cGids */ ); end if;
		if iMinInsertLevel <= 19 and iMaxInsertLevel >= 19 then insert into trackpoints_raw_merge_19( dbs, the_geom, lat, lon, num_points/* , call_id, gids */ ) values( tpRet.depth, geomPoint, tpRet.lat, tpRet.lon, tpRet.num_points/* , iCallId, cGids */ ); end if;
		if iMinInsertLevel <= 20 and iMaxInsertLevel >= 20 then insert into trackpoints_raw_merge_20( dbs, the_geom, lat, lon, num_points/* , call_id, gids */ ) values( tpRet.depth, geomPoint, tpRet.lat, tpRet.lon, tpRet.num_points/* , iCallId, cGids */ ); end if;
		if iMinInsertLevel <= 21 and iMaxInsertLevel >= 21 then insert into trackpoints_raw_merge_21( dbs, the_geom, lat, lon, num_points/* , call_id, gids */ ) values( tpRet.depth, geomPoint, tpRet.lat, tpRet.lon, tpRet.num_points/* , iCallId, cGids */ ); end if;
		if iMinInsertLevel <= 22 and iMaxInsertLevel >= 22 then insert into trackpoints_raw_merge_22( dbs, the_geom, lat, lon, num_points/* , call_id, gids */ ) values( tpRet.depth, geomPoint, tpRet.lat, tpRet.lon, tpRet.num_points/* , iCallId, cGids */ ); end if;
	end if;
	
	if iLevel <= 8 then
		raise notice 'level %: % objects found', iLevel, tpRet.num_points;
	end if;
	
	return tpRet;
end;
$$;


ALTER FUNCTION public.mergetrackpoints(ilevel integer, imaxlevel integer, dleft double precision, dtop double precision, dright double precision, dbottom double precision) OWNER TO postgres;

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
-- Name: prep103forprocessing(integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.prep103forprocessing(imaxtracks integer) RETURNS void
    LANGUAGE plpgsql
    AS $$
declare
 iContTrack bigint;
 iSubTrack bigint;
 iDummy integer;
 bProcess boolean;
 iTracks integer := 0;
begin
 iDummy := osmapi_tables.pullfromosmapi();
 for iContTrack in select x.track_id from osmapi_tables.user_tracks x where upload_state = 103 /*and upr_id = 752*/ 
 and exists( select 1 from osmapi_tables.user_tracks y where containertrack = x.track_id and upload_state = 103 )
 loop
 bProcess := true;
 for iSubTrack in select track_id from osmapi_tables.user_tracks where containertrack = iContTrack 
 loop
 if exists ( select 1 from trackpoints_raw_filter_16 where datasetid = iSubTrack ) then 
 bProcess := false;
 exit;
 end if;
 end loop;
 if bProcess then
 raise notice 'marking containertrack % for processing', iContTrack;
 update user_tracks set upload_state = 3 where track_id = iContTrack or containertrack = iContTrack;
 iTracks := iTracks + 1;
 if iTracks >= iMaxTracks then
 exit;
 end if;
 else
 raise notice 'skipping containertrack % because sub track % contains data', iContTrack, iSubTrack;
 update user_tracks set upload_state = 6 where track_id = iContTrack or containertrack = iContTrack;
 end if;
 end loop;
end;
$$;


ALTER FUNCTION public.prep103forprocessing(imaxtracks integer) OWNER TO postgres;

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
-- Name: resettmptracks(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.resettmptracks() RETURNS void
    LANGUAGE plpgsql
    AS $$
begin
 execute 'truncate table trackpoints_raw_temp_16';
 execute 'truncate table trackpoints_raw_temp_12';
 execute 'truncate table trackpoints_raw_temp_10';
 execute 'truncate table trackpoints_raw_temp_8';
end;
$$;


ALTER FUNCTION public.resettmptracks() OWNER TO postgres;

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
-- Name: sinhd(double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.sinhd(x double precision) RETURNS double precision
    LANGUAGE plpgsql
    AS $$
declare
	dRet double precision;
begin
	dRet := 0.5 * ( exp( radians(x) ) - exp( -radians(x) ) );
	return dRet;
end;
$$;


ALTER FUNCTION public.sinhd(x double precision) OWNER TO postgres;

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
-- Name: user_profiles_id_seq; Type: SEQUENCE; Schema: osmapi_tables; Owner: postgres
--

CREATE SEQUENCE osmapi_tables.user_profiles_id_seq
    START WITH 1
    INCREMENT BY 2
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE osmapi_tables.user_profiles_id_seq OWNER TO postgres;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: user_profiles; Type: TABLE; Schema: osmapi_tables; Owner: postgres
--

CREATE TABLE osmapi_tables.user_profiles (
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
    id bigint DEFAULT nextval('osmapi_tables.user_profiles_id_seq'::regclass) NOT NULL
);


ALTER TABLE osmapi_tables.user_profiles OWNER TO postgres;

--
-- Name: user_tracks_track_id_seq; Type: SEQUENCE; Schema: osmapi_tables; Owner: postgres
--

CREATE SEQUENCE osmapi_tables.user_tracks_track_id_seq
    START WITH 1
    INCREMENT BY 2
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE osmapi_tables.user_tracks_track_id_seq OWNER TO postgres;

--
-- Name: user_tracks; Type: TABLE; Schema: osmapi_tables; Owner: postgres
--

CREATE TABLE osmapi_tables.user_tracks (
    track_id bigint DEFAULT nextval('osmapi_tables.user_tracks_track_id_seq'::regclass) NOT NULL,
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
    uploaddate timestamp without time zone,
    bbox public.geometry,
    clusteruuid character varying,
    clusterseq bigint,
    upr_id bigint NOT NULL,
    num_points bigint,
    is_container boolean DEFAULT false,
    CONSTRAINT enforce_dims_bbox CHECK ((public.st_ndims(bbox) = 2)),
    CONSTRAINT enforce_geotype_bbox CHECK (((public.geometrytype(bbox) = 'POLYGON'::text) OR (bbox IS NULL))),
    CONSTRAINT enforce_srid_bbox CHECK ((public.st_srid(bbox) = 4326))
);


ALTER TABLE osmapi_tables.user_tracks OWNER TO postgres;

--
-- Name: user_tracks; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.user_tracks AS
 SELECT user_tracks.track_id,
    user_profiles.user_name,
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
    user_tracks.is_container
   FROM osmapi_tables.user_tracks,
    osmapi_tables.user_profiles
  WHERE (user_tracks.upr_id = user_profiles.id);


ALTER TABLE public.user_tracks OWNER TO postgres;

--
-- Name: user_tracks_insert_func(public.user_tracks); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.user_tracks_insert_func(rec public.user_tracks) RETURNS void
    LANGUAGE plpgsql
    AS $$
declare
	iUprId bigint;
	iTrackId bigint;
begin
	select id into strict iUprId from user_profiles where user_name = rec.user_name;
	iTrackId := rec.track_id;
	if iTrackId is null then
		iTrackId := nextval( 'osmapi_tables.user_tracks_track_id_seq' );
	end if;
		
	insert into osmapi_tables.user_tracks (
		track_id,
		file_ref,
		upload_state,
		filetype,
		compression,
		containertrack,
		vesselconfigid,
		license,
		gauge_name,
		gauge,
		height_ref,
		comment,
		watertype,
		uploaddate,
		bbox,
		clusteruuid,
		clusterseq,
		upr_id,
		num_points,
		is_container
		)
		values( 
			iTrackId,
			rec.file_ref,
			rec.upload_state,
			rec.filetype,
			rec.compression,
			rec.containertrack,
			rec.vesselconfigid,
			rec.license,
			rec.gauge_name,
			rec.gauge,
			rec.height_ref,
			rec.comment,
			rec.watertype,
			rec.uploaddate,
			rec.bbox,
			rec.clusteruuid,
			rec.clusterseq,
			iUprId,
			rec.num_points,
			rec.is_container
		);
end;
$$;


ALTER FUNCTION public.user_tracks_insert_func(rec public.user_tracks) OWNER TO postgres;

--
-- Name: vesselconfiguration; Type: TABLE; Schema: osmapi_tables; Owner: postgres
--

CREATE TABLE osmapi_tables.vesselconfiguration (
    id integer NOT NULL,
    name character varying,
    description character varying,
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


ALTER TABLE osmapi_tables.vesselconfiguration OWNER TO postgres;

--
-- Name: vesselconfiguration; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.vesselconfiguration AS
 SELECT vesselconfiguration.id,
    vesselconfiguration.name,
    vesselconfiguration.description,
    user_profiles.user_name,
    vesselconfiguration.mmsi,
    vesselconfiguration.manufacturer,
    vesselconfiguration.model,
    vesselconfiguration.loa,
    vesselconfiguration.breadth,
    vesselconfiguration.draft,
    vesselconfiguration.height,
    vesselconfiguration.displacement,
    vesselconfiguration.maximumspeed,
    vesselconfiguration.type,
    vesselconfiguration.upr_id
   FROM osmapi_tables.vesselconfiguration,
    osmapi_tables.user_profiles
  WHERE (vesselconfiguration.upr_id = user_profiles.id);


ALTER TABLE public.vesselconfiguration OWNER TO postgres;

--
-- Name: vesselconfiguration_insert_func(public.vesselconfiguration); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.vesselconfiguration_insert_func(rec public.vesselconfiguration) RETURNS void
    LANGUAGE plpgsql
    AS $$
declare
	iUprId bigint;
	iConfId bigint;
begin
	select id into strict iUprId from user_profiles where user_name = rec.user_name;
	iConfId := rec.Id;
	if iConfId is null then
		iConfId := nextval( 'oosmapi_tables.vesselconfiguration_id_seq' );
	end if;
		
	insert into osmapi_tables.vesselconfiguration (
			id,
			name,
			description,
			user_name,
			mmsi,
			manufacturer,
			model,
			loa,
			breadth,
			draft,
			height,
			displacement,
			maximumspeed,
			type,
			upr_id )
		values( 
			iConfId,
			rec.id,
			rec.name,
			rec.description,
			rec.user_name,
			rec.mmsi,
			rec.manufacturer,
			rec.model,
			rec.loa,
			rec.breadth,
			rec.draft,
			rec.height,
			rec.displacement,
			rec.maximumspeed,
			rec.type,
			iUpr_id );
end;
$$;


ALTER FUNCTION public.vesselconfiguration_insert_func(rec public.vesselconfiguration) OWNER TO postgres;

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
-- Name: osmapi; Type: SERVER; Schema: -; Owner: postgres
--

CREATE SERVER osmapi FOREIGN DATA WRAPPER postgres_fdw OPTIONS (
    dbname 'osmapi',
    host 'postgis',
    port '5432'
);


ALTER SERVER osmapi OWNER TO postgres;

--
-- Name: USER MAPPING public SERVER osmapi; Type: USER MAPPING; Schema: -; Owner: postgres
--

CREATE USER MAPPING FOR public SERVER osmapi OPTIONS (
    "user" 'osmapi'
);


--
-- Name: depthsensor; Type: FOREIGN TABLE; Schema: osmapi_fdw; Owner: postgres
--

CREATE FOREIGN TABLE osmapi_fdw.depthsensor (
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
    id bigint NOT NULL
)
SERVER osmapi
OPTIONS (
    schema_name 'public',
    table_name 'depthsensor'
);
ALTER FOREIGN TABLE osmapi_fdw.depthsensor ALTER COLUMN vesselconfigid OPTIONS (
    column_name 'vesselconfigid'
);
ALTER FOREIGN TABLE osmapi_fdw.depthsensor ALTER COLUMN x OPTIONS (
    column_name 'x'
);
ALTER FOREIGN TABLE osmapi_fdw.depthsensor ALTER COLUMN y OPTIONS (
    column_name 'y'
);
ALTER FOREIGN TABLE osmapi_fdw.depthsensor ALTER COLUMN z OPTIONS (
    column_name 'z'
);
ALTER FOREIGN TABLE osmapi_fdw.depthsensor ALTER COLUMN sensorid OPTIONS (
    column_name 'sensorid'
);
ALTER FOREIGN TABLE osmapi_fdw.depthsensor ALTER COLUMN manufacturer OPTIONS (
    column_name 'manufacturer'
);
ALTER FOREIGN TABLE osmapi_fdw.depthsensor ALTER COLUMN model OPTIONS (
    column_name 'model'
);
ALTER FOREIGN TABLE osmapi_fdw.depthsensor ALTER COLUMN frequency OPTIONS (
    column_name 'frequency'
);
ALTER FOREIGN TABLE osmapi_fdw.depthsensor ALTER COLUMN angleofbeam OPTIONS (
    column_name 'angleofbeam'
);
ALTER FOREIGN TABLE osmapi_fdw.depthsensor ALTER COLUMN offsetkeel OPTIONS (
    column_name 'offsetkeel'
);
ALTER FOREIGN TABLE osmapi_fdw.depthsensor ALTER COLUMN offsettype OPTIONS (
    column_name 'offsettype'
);
ALTER FOREIGN TABLE osmapi_fdw.depthsensor ALTER COLUMN id OPTIONS (
    column_name 'id'
);


ALTER FOREIGN TABLE osmapi_fdw.depthsensor OWNER TO postgres;

--
-- Name: rpl_journal; Type: FOREIGN TABLE; Schema: osmapi_fdw; Owner: postgres
--

CREATE FOREIGN TABLE osmapi_fdw.rpl_journal (
    id bigint NOT NULL,
    table_name character varying(50) NOT NULL,
    row_id bigint NOT NULL,
    opcode character varying(1) NOT NULL,
    time_stamp timestamp without time zone NOT NULL
)
SERVER osmapi
OPTIONS (
    schema_name 'public',
    table_name 'rpl_journal'
);
ALTER FOREIGN TABLE osmapi_fdw.rpl_journal ALTER COLUMN id OPTIONS (
    column_name 'id'
);
ALTER FOREIGN TABLE osmapi_fdw.rpl_journal ALTER COLUMN table_name OPTIONS (
    column_name 'table_name'
);
ALTER FOREIGN TABLE osmapi_fdw.rpl_journal ALTER COLUMN row_id OPTIONS (
    column_name 'row_id'
);
ALTER FOREIGN TABLE osmapi_fdw.rpl_journal ALTER COLUMN opcode OPTIONS (
    column_name 'opcode'
);
ALTER FOREIGN TABLE osmapi_fdw.rpl_journal ALTER COLUMN time_stamp OPTIONS (
    column_name 'time_stamp'
);


ALTER FOREIGN TABLE osmapi_fdw.rpl_journal OWNER TO postgres;

--
-- Name: sbassensor; Type: FOREIGN TABLE; Schema: osmapi_fdw; Owner: postgres
--

CREATE FOREIGN TABLE osmapi_fdw.sbassensor (
    vesselconfigid integer NOT NULL,
    x numeric(5,2),
    y numeric(5,2),
    z numeric(5,2),
    sensorid character varying,
    manufacturer character varying(100),
    model character varying(100),
    id bigint NOT NULL
)
SERVER osmapi
OPTIONS (
    schema_name 'public',
    table_name 'sbassensor'
);
ALTER FOREIGN TABLE osmapi_fdw.sbassensor ALTER COLUMN vesselconfigid OPTIONS (
    column_name 'vesselconfigid'
);
ALTER FOREIGN TABLE osmapi_fdw.sbassensor ALTER COLUMN x OPTIONS (
    column_name 'x'
);
ALTER FOREIGN TABLE osmapi_fdw.sbassensor ALTER COLUMN y OPTIONS (
    column_name 'y'
);
ALTER FOREIGN TABLE osmapi_fdw.sbassensor ALTER COLUMN z OPTIONS (
    column_name 'z'
);
ALTER FOREIGN TABLE osmapi_fdw.sbassensor ALTER COLUMN sensorid OPTIONS (
    column_name 'sensorid'
);
ALTER FOREIGN TABLE osmapi_fdw.sbassensor ALTER COLUMN manufacturer OPTIONS (
    column_name 'manufacturer'
);
ALTER FOREIGN TABLE osmapi_fdw.sbassensor ALTER COLUMN model OPTIONS (
    column_name 'model'
);
ALTER FOREIGN TABLE osmapi_fdw.sbassensor ALTER COLUMN id OPTIONS (
    column_name 'id'
);


ALTER FOREIGN TABLE osmapi_fdw.sbassensor OWNER TO postgres;

--
-- Name: track_info; Type: FOREIGN TABLE; Schema: osmapi_fdw; Owner: postgres
--

CREATE FOREIGN TABLE osmapi_fdw.track_info (
    id bigint NOT NULL,
    tra_id bigint NOT NULL,
    short_info character varying(20),
    long_info character varying,
    reprocess boolean,
    discard boolean,
    ignore boolean
)
SERVER osmapi
OPTIONS (
    schema_name 'public',
    table_name 'track_info'
);
ALTER FOREIGN TABLE osmapi_fdw.track_info ALTER COLUMN id OPTIONS (
    column_name 'id'
);
ALTER FOREIGN TABLE osmapi_fdw.track_info ALTER COLUMN tra_id OPTIONS (
    column_name 'tra_id'
);
ALTER FOREIGN TABLE osmapi_fdw.track_info ALTER COLUMN short_info OPTIONS (
    column_name 'short_info'
);
ALTER FOREIGN TABLE osmapi_fdw.track_info ALTER COLUMN long_info OPTIONS (
    column_name 'long_info'
);
ALTER FOREIGN TABLE osmapi_fdw.track_info ALTER COLUMN reprocess OPTIONS (
    column_name 'reprocess'
);
ALTER FOREIGN TABLE osmapi_fdw.track_info ALTER COLUMN discard OPTIONS (
    column_name 'discard'
);
ALTER FOREIGN TABLE osmapi_fdw.track_info ALTER COLUMN ignore OPTIONS (
    column_name 'ignore'
);


ALTER FOREIGN TABLE osmapi_fdw.track_info OWNER TO postgres;

--
-- Name: user_profiles; Type: FOREIGN TABLE; Schema: osmapi_fdw; Owner: postgres
--

CREATE FOREIGN TABLE osmapi_fdw.user_profiles (
    user_name character varying(256) NOT NULL,
    password character varying(40),
    salt character varying(10),
    attempts smallint NOT NULL,
    last_attempt timestamp without time zone,
    forename character varying,
    surname character varying,
    country character varying,
    language character varying,
    organisation character varying,
    phone character varying,
    acceptedemailcontact boolean,
    id bigint NOT NULL
)
SERVER osmapi
OPTIONS (
    schema_name 'public',
    table_name 'user_profiles'
);
ALTER FOREIGN TABLE osmapi_fdw.user_profiles ALTER COLUMN user_name OPTIONS (
    column_name 'user_name'
);
ALTER FOREIGN TABLE osmapi_fdw.user_profiles ALTER COLUMN password OPTIONS (
    column_name 'password'
);
ALTER FOREIGN TABLE osmapi_fdw.user_profiles ALTER COLUMN salt OPTIONS (
    column_name 'salt'
);
ALTER FOREIGN TABLE osmapi_fdw.user_profiles ALTER COLUMN attempts OPTIONS (
    column_name 'attempts'
);
ALTER FOREIGN TABLE osmapi_fdw.user_profiles ALTER COLUMN last_attempt OPTIONS (
    column_name 'last_attempt'
);
ALTER FOREIGN TABLE osmapi_fdw.user_profiles ALTER COLUMN forename OPTIONS (
    column_name 'forename'
);
ALTER FOREIGN TABLE osmapi_fdw.user_profiles ALTER COLUMN surname OPTIONS (
    column_name 'surname'
);
ALTER FOREIGN TABLE osmapi_fdw.user_profiles ALTER COLUMN country OPTIONS (
    column_name 'country'
);
ALTER FOREIGN TABLE osmapi_fdw.user_profiles ALTER COLUMN language OPTIONS (
    column_name 'language'
);
ALTER FOREIGN TABLE osmapi_fdw.user_profiles ALTER COLUMN organisation OPTIONS (
    column_name 'organisation'
);
ALTER FOREIGN TABLE osmapi_fdw.user_profiles ALTER COLUMN phone OPTIONS (
    column_name 'phone'
);
ALTER FOREIGN TABLE osmapi_fdw.user_profiles ALTER COLUMN acceptedemailcontact OPTIONS (
    column_name 'acceptedemailcontact'
);
ALTER FOREIGN TABLE osmapi_fdw.user_profiles ALTER COLUMN id OPTIONS (
    column_name 'id'
);


ALTER FOREIGN TABLE osmapi_fdw.user_profiles OWNER TO postgres;

--
-- Name: user_tracks; Type: FOREIGN TABLE; Schema: osmapi_fdw; Owner: postgres
--

CREATE FOREIGN TABLE osmapi_fdw.user_tracks (
    track_id bigint NOT NULL,
    user_name character varying(40) NOT NULL,
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
    num_points integer,
    is_container boolean
)
SERVER osmapi
OPTIONS (
    schema_name 'public',
    table_name 'user_tracks'
);
ALTER FOREIGN TABLE osmapi_fdw.user_tracks ALTER COLUMN track_id OPTIONS (
    column_name 'track_id'
);
ALTER FOREIGN TABLE osmapi_fdw.user_tracks ALTER COLUMN user_name OPTIONS (
    column_name 'user_name'
);
ALTER FOREIGN TABLE osmapi_fdw.user_tracks ALTER COLUMN file_ref OPTIONS (
    column_name 'file_ref'
);
ALTER FOREIGN TABLE osmapi_fdw.user_tracks ALTER COLUMN upload_state OPTIONS (
    column_name 'upload_state'
);
ALTER FOREIGN TABLE osmapi_fdw.user_tracks ALTER COLUMN filetype OPTIONS (
    column_name 'filetype'
);
ALTER FOREIGN TABLE osmapi_fdw.user_tracks ALTER COLUMN compression OPTIONS (
    column_name 'compression'
);
ALTER FOREIGN TABLE osmapi_fdw.user_tracks ALTER COLUMN containertrack OPTIONS (
    column_name 'containertrack'
);
ALTER FOREIGN TABLE osmapi_fdw.user_tracks ALTER COLUMN vesselconfigid OPTIONS (
    column_name 'vesselconfigid'
);
ALTER FOREIGN TABLE osmapi_fdw.user_tracks ALTER COLUMN license OPTIONS (
    column_name 'license'
);
ALTER FOREIGN TABLE osmapi_fdw.user_tracks ALTER COLUMN gauge_name OPTIONS (
    column_name 'gauge_name'
);
ALTER FOREIGN TABLE osmapi_fdw.user_tracks ALTER COLUMN gauge OPTIONS (
    column_name 'gauge'
);
ALTER FOREIGN TABLE osmapi_fdw.user_tracks ALTER COLUMN height_ref OPTIONS (
    column_name 'height_ref'
);
ALTER FOREIGN TABLE osmapi_fdw.user_tracks ALTER COLUMN comment OPTIONS (
    column_name 'comment'
);
ALTER FOREIGN TABLE osmapi_fdw.user_tracks ALTER COLUMN watertype OPTIONS (
    column_name 'watertype'
);
ALTER FOREIGN TABLE osmapi_fdw.user_tracks ALTER COLUMN uploaddate OPTIONS (
    column_name 'uploaddate'
);
ALTER FOREIGN TABLE osmapi_fdw.user_tracks ALTER COLUMN bbox OPTIONS (
    column_name 'bbox'
);
ALTER FOREIGN TABLE osmapi_fdw.user_tracks ALTER COLUMN clusteruuid OPTIONS (
    column_name 'clusteruuid'
);
ALTER FOREIGN TABLE osmapi_fdw.user_tracks ALTER COLUMN clusterseq OPTIONS (
    column_name 'clusterseq'
);
ALTER FOREIGN TABLE osmapi_fdw.user_tracks ALTER COLUMN upr_id OPTIONS (
    column_name 'upr_id'
);
ALTER FOREIGN TABLE osmapi_fdw.user_tracks ALTER COLUMN num_points OPTIONS (
    column_name 'num_points'
);
ALTER FOREIGN TABLE osmapi_fdw.user_tracks ALTER COLUMN is_container OPTIONS (
    column_name 'is_container'
);


ALTER FOREIGN TABLE osmapi_fdw.user_tracks OWNER TO postgres;

--
-- Name: vesselconfiguration; Type: FOREIGN TABLE; Schema: osmapi_fdw; Owner: postgres
--

CREATE FOREIGN TABLE osmapi_fdw.vesselconfiguration (
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
    type integer,
    upr_id bigint NOT NULL
)
SERVER osmapi
OPTIONS (
    schema_name 'public',
    table_name 'vesselconfiguration'
);
ALTER FOREIGN TABLE osmapi_fdw.vesselconfiguration ALTER COLUMN id OPTIONS (
    column_name 'id'
);
ALTER FOREIGN TABLE osmapi_fdw.vesselconfiguration ALTER COLUMN name OPTIONS (
    column_name 'name'
);
ALTER FOREIGN TABLE osmapi_fdw.vesselconfiguration ALTER COLUMN description OPTIONS (
    column_name 'description'
);
ALTER FOREIGN TABLE osmapi_fdw.vesselconfiguration ALTER COLUMN user_name OPTIONS (
    column_name 'user_name'
);
ALTER FOREIGN TABLE osmapi_fdw.vesselconfiguration ALTER COLUMN mmsi OPTIONS (
    column_name 'mmsi'
);
ALTER FOREIGN TABLE osmapi_fdw.vesselconfiguration ALTER COLUMN manufacturer OPTIONS (
    column_name 'manufacturer'
);
ALTER FOREIGN TABLE osmapi_fdw.vesselconfiguration ALTER COLUMN model OPTIONS (
    column_name 'model'
);
ALTER FOREIGN TABLE osmapi_fdw.vesselconfiguration ALTER COLUMN loa OPTIONS (
    column_name 'loa'
);
ALTER FOREIGN TABLE osmapi_fdw.vesselconfiguration ALTER COLUMN breadth OPTIONS (
    column_name 'breadth'
);
ALTER FOREIGN TABLE osmapi_fdw.vesselconfiguration ALTER COLUMN draft OPTIONS (
    column_name 'draft'
);
ALTER FOREIGN TABLE osmapi_fdw.vesselconfiguration ALTER COLUMN height OPTIONS (
    column_name 'height'
);
ALTER FOREIGN TABLE osmapi_fdw.vesselconfiguration ALTER COLUMN displacement OPTIONS (
    column_name 'displacement'
);
ALTER FOREIGN TABLE osmapi_fdw.vesselconfiguration ALTER COLUMN maximumspeed OPTIONS (
    column_name 'maximumspeed'
);
ALTER FOREIGN TABLE osmapi_fdw.vesselconfiguration ALTER COLUMN type OPTIONS (
    column_name 'type'
);
ALTER FOREIGN TABLE osmapi_fdw.vesselconfiguration ALTER COLUMN upr_id OPTIONS (
    column_name 'upr_id'
);


ALTER FOREIGN TABLE osmapi_fdw.vesselconfiguration OWNER TO postgres;

--
-- Name: depthsensor_id_seq; Type: SEQUENCE; Schema: osmapi_tables; Owner: postgres
--

CREATE SEQUENCE osmapi_tables.depthsensor_id_seq
    START WITH 1
    INCREMENT BY 2
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE osmapi_tables.depthsensor_id_seq OWNER TO postgres;

--
-- Name: depthsensor; Type: TABLE; Schema: osmapi_tables; Owner: postgres
--

CREATE TABLE osmapi_tables.depthsensor (
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
    id bigint DEFAULT nextval('osmapi_tables.depthsensor_id_seq'::regclass) NOT NULL
);


ALTER TABLE osmapi_tables.depthsensor OWNER TO postgres;

--
-- Name: gauge; Type: TABLE; Schema: osmapi_tables; Owner: postgres
--

CREATE TABLE osmapi_tables.gauge (
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


ALTER TABLE osmapi_tables.gauge OWNER TO postgres;

--
-- Name: gauge_id_seq; Type: SEQUENCE; Schema: osmapi_tables; Owner: postgres
--

CREATE SEQUENCE osmapi_tables.gauge_id_seq
    START WITH 20
    INCREMENT BY 2
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE osmapi_tables.gauge_id_seq OWNER TO postgres;

--
-- Name: gaugemeasurement; Type: TABLE; Schema: osmapi_tables; Owner: postgres
--

CREATE TABLE osmapi_tables.gaugemeasurement (
    gaugeid integer NOT NULL,
    value numeric(4,2) NOT NULL,
    "time" timestamp without time zone NOT NULL
);


ALTER TABLE osmapi_tables.gaugemeasurement OWNER TO postgres;

--
-- Name: license; Type: TABLE; Schema: osmapi_tables; Owner: postgres
--

CREATE TABLE osmapi_tables.license (
    name character varying(255) NOT NULL,
    shortname character varying(16),
    text text,
    osmapi_tables boolean,
    id integer NOT NULL,
    user_name character varying(255)
);


ALTER TABLE osmapi_tables.license OWNER TO postgres;

--
-- Name: license_id_seq; Type: SEQUENCE; Schema: osmapi_tables; Owner: postgres
--

CREATE SEQUENCE osmapi_tables.license_id_seq
    START WITH 1
    INCREMENT BY 2
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE osmapi_tables.license_id_seq OWNER TO postgres;

--
-- Name: license_id_seq; Type: SEQUENCE OWNED BY; Schema: osmapi_tables; Owner: postgres
--

ALTER SEQUENCE osmapi_tables.license_id_seq OWNED BY osmapi_tables.license.id;


--
-- Name: repl_id_seq; Type: SEQUENCE; Schema: osmapi_tables; Owner: postgres
--

CREATE SEQUENCE osmapi_tables.repl_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE osmapi_tables.repl_id_seq OWNER TO postgres;

--
-- Name: rpl_journal; Type: TABLE; Schema: osmapi_tables; Owner: postgres
--

CREATE TABLE osmapi_tables.rpl_journal (
    id bigint DEFAULT nextval('osmapi_tables.repl_id_seq'::regclass) NOT NULL,
    table_name character varying(50) NOT NULL,
    row_id bigint NOT NULL,
    opcode character varying(1) NOT NULL,
    time_stamp timestamp without time zone DEFAULT now() NOT NULL
);


ALTER TABLE osmapi_tables.rpl_journal OWNER TO postgres;

--
-- Name: rpl_journal_shadow; Type: TABLE; Schema: osmapi_tables; Owner: postgres
--

CREATE TABLE osmapi_tables.rpl_journal_shadow (
    id bigint NOT NULL,
    table_name character varying(50) NOT NULL,
    row_id bigint NOT NULL,
    opcode character varying(1) NOT NULL,
    time_stamp timestamp without time zone NOT NULL,
    copied timestamp without time zone
);


ALTER TABLE osmapi_tables.rpl_journal_shadow OWNER TO postgres;

--
-- Name: sbassensor_id_seq; Type: SEQUENCE; Schema: osmapi_tables; Owner: postgres
--

CREATE SEQUENCE osmapi_tables.sbassensor_id_seq
    START WITH 1
    INCREMENT BY 2
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE osmapi_tables.sbassensor_id_seq OWNER TO postgres;

--
-- Name: sbassensor; Type: TABLE; Schema: osmapi_tables; Owner: postgres
--

CREATE TABLE osmapi_tables.sbassensor (
    vesselconfigid integer NOT NULL,
    x numeric(5,2),
    y numeric(5,2),
    z numeric(5,2),
    sensorid character varying,
    manufacturer character varying(100),
    model character varying(100),
    id bigint DEFAULT nextval('osmapi_tables.sbassensor_id_seq'::regclass) NOT NULL
);


ALTER TABLE osmapi_tables.sbassensor OWNER TO postgres;

--
-- Name: seq_tif; Type: SEQUENCE; Schema: osmapi_tables; Owner: postgres
--

CREATE SEQUENCE osmapi_tables.seq_tif
    START WITH 1
    INCREMENT BY 2
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE osmapi_tables.seq_tif OWNER TO postgres;

--
-- Name: track_info; Type: TABLE; Schema: osmapi_tables; Owner: postgres
--

CREATE TABLE osmapi_tables.track_info (
    id bigint DEFAULT nextval('osmapi_tables.seq_tif'::regclass) NOT NULL,
    tra_id bigint NOT NULL,
    short_info character varying(20),
    long_info character varying,
    reprocess boolean DEFAULT false,
    discard boolean DEFAULT false,
    ignore boolean DEFAULT true
);


ALTER TABLE osmapi_tables.track_info OWNER TO postgres;

--
-- Name: trackgauges; Type: TABLE; Schema: osmapi_tables; Owner: postgres
--

CREATE TABLE osmapi_tables.trackgauges (
    id integer NOT NULL,
    trackid bigint,
    gaugeid integer,
    source integer
);


ALTER TABLE osmapi_tables.trackgauges OWNER TO postgres;

--
-- Name: trackgauges_id_seq; Type: SEQUENCE; Schema: osmapi_tables; Owner: postgres
--

CREATE SEQUENCE osmapi_tables.trackgauges_id_seq
    START WITH 1
    INCREMENT BY 2
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE osmapi_tables.trackgauges_id_seq OWNER TO postgres;

--
-- Name: trackgauges_id_seq; Type: SEQUENCE OWNED BY; Schema: osmapi_tables; Owner: postgres
--

ALTER SEQUENCE osmapi_tables.trackgauges_id_seq OWNED BY osmapi_tables.trackgauges.id;


--
-- Name: userroles; Type: TABLE; Schema: osmapi_tables; Owner: postgres
--

CREATE TABLE osmapi_tables.userroles (
    user_name character varying(250),
    role character varying(15)
);


ALTER TABLE osmapi_tables.userroles OWNER TO postgres;

--
-- Name: vesselconfiguration_id_seq; Type: SEQUENCE; Schema: osmapi_tables; Owner: postgres
--

CREATE SEQUENCE osmapi_tables.vesselconfiguration_id_seq
    START WITH 1
    INCREMENT BY 2
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE osmapi_tables.vesselconfiguration_id_seq OWNER TO postgres;

--
-- Name: vesselconfiguration_id_seq; Type: SEQUENCE OWNED BY; Schema: osmapi_tables; Owner: postgres
--

ALTER SEQUENCE osmapi_tables.vesselconfiguration_id_seq OWNED BY osmapi_tables.vesselconfiguration.id;


--
-- Name: 1000er; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."1000er" (
    gid integer NOT NULL,
    m integer,
    the_geom public.geometry,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.st_ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((public.geometrytype(the_geom) = 'MULTILINESTRING'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.st_srid(the_geom) = 4326))
);


ALTER TABLE public."1000er" OWNER TO postgres;

--
-- Name: 1000er_gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public."1000er_gid_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public."1000er_gid_seq" OWNER TO postgres;

--
-- Name: 1000er_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public."1000er_gid_seq" OWNED BY public."1000er".gid;


--
-- Name: 2000er; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."2000er" (
    gid integer NOT NULL,
    m integer,
    the_geom public.geometry,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.st_ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((public.geometrytype(the_geom) = 'MULTILINESTRING'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.st_srid(the_geom) = 4326))
);


ALTER TABLE public."2000er" OWNER TO postgres;

--
-- Name: 2000er_gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public."2000er_gid_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public."2000er_gid_seq" OWNER TO postgres;

--
-- Name: 2000er_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public."2000er_gid_seq" OWNED BY public."2000er".gid;


--
-- Name: big_polygon; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.big_polygon (
    geom public.geometry,
    gid integer NOT NULL
);


ALTER TABLE public.big_polygon OWNER TO postgres;

--
-- Name: big_polygon2; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.big_polygon2 (
    geom public.geometry,
    gid integer NOT NULL
);


ALTER TABLE public.big_polygon2 OWNER TO postgres;

--
-- Name: big_polygon2_gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.big_polygon2_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.big_polygon2_gid_seq OWNER TO postgres;

--
-- Name: big_polygon2_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.big_polygon2_gid_seq OWNED BY public.big_polygon2.gid;


--
-- Name: big_polygon_gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.big_polygon_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.big_polygon_gid_seq OWNER TO postgres;

--
-- Name: big_polygon_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.big_polygon_gid_seq OWNED BY public.big_polygon.gid;


--
-- Name: brom_difference; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.brom_difference (
    gid integer NOT NULL,
    dif numeric,
    diff smallint,
    the_geom public.geometry,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.st_ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((public.geometrytype(the_geom) = 'POINT'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.st_srid(the_geom) = 4326))
);


ALTER TABLE public.brom_difference OWNER TO postgres;

--
-- Name: brom_difference_gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.brom_difference_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.brom_difference_gid_seq OWNER TO postgres;

--
-- Name: brom_difference_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.brom_difference_gid_seq OWNED BY public.brom_difference.gid;


--
-- Name: bsh_points; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.bsh_points (
    gid integer NOT NULL,
    x character varying(80),
    y character varying(80),
    dbs character varying(80),
    the_geom public.geometry,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.st_ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((public.geometrytype(the_geom) = 'POINT'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.st_srid(the_geom) = 4326))
);


ALTER TABLE public.bsh_points OWNER TO postgres;

--
-- Name: bsh_points_gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.bsh_points_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.bsh_points_gid_seq OWNER TO postgres;

--
-- Name: bsh_points_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.bsh_points_gid_seq OWNED BY public.bsh_points.gid;


--
-- Name: bsh_zoom_10_cor_1_points; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.bsh_zoom_10_cor_1_points (
    gid integer NOT NULL,
    the_geom public.geometry
);


ALTER TABLE public.bsh_zoom_10_cor_1_points OWNER TO postgres;

--
-- Name: bsh_zoom_2_cor_1_points; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.bsh_zoom_2_cor_1_points (
    gid integer NOT NULL,
    the_geom public.geometry
);


ALTER TABLE public.bsh_zoom_2_cor_1_points OWNER TO postgres;

--
-- Name: bsh_zoom_3_cor_1_points; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.bsh_zoom_3_cor_1_points (
    gid integer NOT NULL,
    the_geom public.geometry
);


ALTER TABLE public.bsh_zoom_3_cor_1_points OWNER TO postgres;

--
-- Name: bsh_zoom_4_cor_1_points; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.bsh_zoom_4_cor_1_points (
    gid integer NOT NULL,
    the_geom public.geometry
);


ALTER TABLE public.bsh_zoom_4_cor_1_points OWNER TO postgres;

--
-- Name: bsh_zoom_5_cor_1_points; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.bsh_zoom_5_cor_1_points (
    gid integer NOT NULL,
    the_geom public.geometry
);


ALTER TABLE public.bsh_zoom_5_cor_1_points OWNER TO postgres;

--
-- Name: bsh_zoom_6_cor_1_points; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.bsh_zoom_6_cor_1_points (
    gid integer NOT NULL,
    the_geom public.geometry
);


ALTER TABLE public.bsh_zoom_6_cor_1_points OWNER TO postgres;

--
-- Name: bsh_zoom_7_cor_1_points; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.bsh_zoom_7_cor_1_points (
    gid integer NOT NULL,
    the_geom public.geometry
);


ALTER TABLE public.bsh_zoom_7_cor_1_points OWNER TO postgres;

--
-- Name: bsh_zoom_8_cor_1_points; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.bsh_zoom_8_cor_1_points (
    gid integer NOT NULL,
    the_geom public.geometry
);


ALTER TABLE public.bsh_zoom_8_cor_1_points OWNER TO postgres;

--
-- Name: bsh_zoom_9_cor_1_points; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.bsh_zoom_9_cor_1_points (
    gid integer NOT NULL,
    the_geom public.geometry
);


ALTER TABLE public.bsh_zoom_9_cor_1_points OWNER TO postgres;

--
-- Name: call_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.call_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.call_id_seq OWNER TO postgres;

--
-- Name: contourmanual; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.contourmanual (
    ogc_fid integer NOT NULL,
    wkb_geometry public.geometry,
    id numeric(8,0),
    elevation numeric(12,3),
    CONSTRAINT enforce_dims_wkb_geometry CHECK ((public.st_ndims(wkb_geometry) = 2)),
    CONSTRAINT enforce_geotype_wkb_geometry CHECK (((public.geometrytype(wkb_geometry) = 'LINESTRING'::text) OR (wkb_geometry IS NULL))),
    CONSTRAINT enforce_srid_wkb_geometry CHECK ((public.st_srid(wkb_geometry) = 4326))
);


ALTER TABLE public.contourmanual OWNER TO postgres;

--
-- Name: contourmanual_ogc_fid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.contourmanual_ogc_fid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.contourmanual_ogc_fid_seq OWNER TO postgres;

--
-- Name: contourmanual_ogc_fid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.contourmanual_ogc_fid_seq OWNED BY public.contourmanual.ogc_fid;


--
-- Name: contours2; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.contours2 (
    m integer,
    source bigint,
    the_geom public.geometry
);


ALTER TABLE public.contours2 OWNER TO postgres;

--
-- Name: contours_cor1; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.contours_cor1 (
    gid integer NOT NULL,
    id numeric(10,0),
    contour numeric(14,0),
    the_geom public.geometry,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.st_ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((public.geometrytype(the_geom) = 'MULTILINESTRING'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.st_srid(the_geom) = 4326))
);


ALTER TABLE public.contours_cor1 OWNER TO postgres;

--
-- Name: contours_cor1_gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.contours_cor1_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.contours_cor1_gid_seq OWNER TO postgres;

--
-- Name: contours_cor1_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.contours_cor1_gid_seq OWNED BY public.contours_cor1.gid;


--
-- Name: contours_cor_1; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.contours_cor_1 (
    gid integer NOT NULL,
    id numeric(10,0),
    contour numeric(14,0),
    the_geom public.geometry,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.st_ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((public.geometrytype(the_geom) = 'MULTILINESTRING'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.st_srid(the_geom) = 4326))
);


ALTER TABLE public.contours_cor_1 OWNER TO postgres;

--
-- Name: contours_cor_1_gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.contours_cor_1_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.contours_cor_1_gid_seq OWNER TO postgres;

--
-- Name: contours_cor_1_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.contours_cor_1_gid_seq OWNED BY public.contours_cor_1.gid;


SET default_with_oids = true;

--
-- Name: contoursplit; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.contoursplit (
    id integer NOT NULL,
    m integer,
    source bigint,
    the_geom public.geometry,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.st_ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((public.geometrytype(the_geom) = 'LINESTRING'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.st_srid(the_geom) = 4326))
);


ALTER TABLE public.contoursplit OWNER TO postgres;

--
-- Name: contours_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.contours_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.contours_id_seq OWNER TO postgres;

--
-- Name: contours_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.contours_id_seq OWNED BY public.contoursplit.id;


SET default_with_oids = false;

--
-- Name: contours_zoom_10_cor_1; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.contours_zoom_10_cor_1 (
    gid integer,
    the_geom public.geometry
);


ALTER TABLE public.contours_zoom_10_cor_1 OWNER TO postgres;

--
-- Name: contours_zoom_2_cor_1; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.contours_zoom_2_cor_1 (
    gid integer,
    the_geom public.geometry
);


ALTER TABLE public.contours_zoom_2_cor_1 OWNER TO postgres;

--
-- Name: contours_zoom_3_cor_1; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.contours_zoom_3_cor_1 (
    gid integer,
    the_geom public.geometry
);


ALTER TABLE public.contours_zoom_3_cor_1 OWNER TO postgres;

--
-- Name: contours_zoom_4_cor_1; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.contours_zoom_4_cor_1 (
    gid integer,
    the_geom public.geometry
);


ALTER TABLE public.contours_zoom_4_cor_1 OWNER TO postgres;

--
-- Name: contours_zoom_5_cor_1; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.contours_zoom_5_cor_1 (
    gid integer,
    the_geom public.geometry
);


ALTER TABLE public.contours_zoom_5_cor_1 OWNER TO postgres;

--
-- Name: contours_zoom_6_cor_1; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.contours_zoom_6_cor_1 (
    gid integer,
    the_geom public.geometry
);


ALTER TABLE public.contours_zoom_6_cor_1 OWNER TO postgres;

--
-- Name: contours_zoom_7_cor_1; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.contours_zoom_7_cor_1 (
    gid integer,
    the_geom public.geometry
);


ALTER TABLE public.contours_zoom_7_cor_1 OWNER TO postgres;

--
-- Name: contours_zoom_8_cor_1; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.contours_zoom_8_cor_1 (
    gid integer,
    the_geom public.geometry
);


ALTER TABLE public.contours_zoom_8_cor_1 OWNER TO postgres;

--
-- Name: contours_zoom_9_cor_1; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.contours_zoom_9_cor_1 (
    gid integer,
    the_geom public.geometry
);


ALTER TABLE public.contours_zoom_9_cor_1 OWNER TO postgres;

--
-- Name: contourspitmulti; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.contourspitmulti (
    gid integer NOT NULL,
    m integer,
    the_geom public.geometry,
    source bigint,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.st_ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((public.geometrytype(the_geom) = 'MULTILINESTRING'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.st_srid(the_geom) = 4326))
);


ALTER TABLE public.contourspitmulti OWNER TO postgres;

--
-- Name: contoursplit_gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.contoursplit_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.contoursplit_gid_seq OWNER TO postgres;

--
-- Name: contoursplit_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.contoursplit_gid_seq OWNED BY public.contourspitmulti.gid;


--
-- Name: crowded_deeps; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.crowded_deeps (
    gid integer NOT NULL,
    "depth (m)" integer,
    "depth (m_1" double precision,
    "depth (ft)" integer,
    "depth (f_1" double precision,
    m smallint,
    the_geom public.geometry,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.st_ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((public.geometrytype(the_geom) = 'MULTILINESTRING'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.st_srid(the_geom) = 4326))
);


ALTER TABLE public.crowded_deeps OWNER TO postgres;

--
-- Name: crowded_deeps_gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.crowded_deeps_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.crowded_deeps_gid_seq OWNER TO postgres;

--
-- Name: crowded_deeps_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.crowded_deeps_gid_seq OWNED BY public.crowded_deeps.gid;


--
-- Name: crowded_osm; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.crowded_osm (
    gid integer NOT NULL,
    m smallint,
    the_geom public.geometry,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.st_ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((public.geometrytype(the_geom) = 'MULTILINESTRING'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.st_srid(the_geom) = 4326))
);


ALTER TABLE public.crowded_osm OWNER TO postgres;

--
-- Name: crowded_osm_gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.crowded_osm_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.crowded_osm_gid_seq OWNER TO postgres;

--
-- Name: crowded_osm_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.crowded_osm_gid_seq OWNED BY public.crowded_osm.gid;


--
-- Name: crowed_deeps; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.crowed_deeps (
    gid integer NOT NULL,
    "depth (m)" integer,
    "depth (m)__1" double precision,
    "depth (ft)" integer,
    "depth (ft)__3" double precision,
    the_geom public.geometry,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.st_ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((public.geometrytype(the_geom) = 'MULTILINESTRING'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.st_srid(the_geom) = 4326))
);


ALTER TABLE public.crowed_deeps OWNER TO postgres;

--
-- Name: crowed_deeps_gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.crowed_deeps_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.crowed_deeps_gid_seq OWNER TO postgres;

--
-- Name: crowed_deeps_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.crowed_deeps_gid_seq OWNED BY public.crowed_deeps.gid;


--
-- Name: depthsensor; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.depthsensor AS
 SELECT depthsensor.vesselconfigid,
    depthsensor.x,
    depthsensor.y,
    depthsensor.z,
    depthsensor.sensorid,
    depthsensor.manufacturer,
    depthsensor.model,
    depthsensor.frequency,
    depthsensor.angleofbeam,
    depthsensor.offsetkeel,
    depthsensor.offsettype,
    depthsensor.id
   FROM osmapi_tables.depthsensor;


ALTER TABLE public.depthsensor OWNER TO postgres;

--
-- Name: gebco_contours_2014; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.gebco_contours_2014 (
    gid integer NOT NULL,
    objectid integer,
    depth smallint,
    shape_leng numeric,
    geom public.geometry,
    CONSTRAINT enforce_dims_geom CHECK ((public.st_ndims(geom) = 2)),
    CONSTRAINT enforce_geotype_geom CHECK (((public.geometrytype(geom) = 'MULTILINESTRING'::text) OR (geom IS NULL))),
    CONSTRAINT enforce_srid_geom CHECK ((public.st_srid(geom) = 4326))
);


ALTER TABLE public.gebco_contours_2014 OWNER TO postgres;

--
-- Name: gebco_contours_2014_gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.gebco_contours_2014_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.gebco_contours_2014_gid_seq OWNER TO postgres;

--
-- Name: gebco_contours_2014_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.gebco_contours_2014_gid_seq OWNED BY public.gebco_contours_2014.gid;


--
-- Name: gebco_poly_100; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.gebco_poly_100 (
    gid integer NOT NULL,
    objectid_1 integer,
    fid_water_ integer,
    ogc_fid character varying(21),
    fid_gebco_ integer,
    objectid integer,
    gridcode integer,
    shape_leng numeric,
    shape_le_1 numeric,
    shape_area numeric,
    geom public.geometry,
    CONSTRAINT enforce_dims_geom CHECK ((public.st_ndims(geom) = 2)),
    CONSTRAINT enforce_geotype_geom CHECK (((public.geometrytype(geom) = 'MULTIPOLYGON'::text) OR (geom IS NULL))),
    CONSTRAINT enforce_srid_geom CHECK ((public.st_srid(geom) = 4326))
);


ALTER TABLE public.gebco_poly_100 OWNER TO postgres;

--
-- Name: gebco_poly_100_gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.gebco_poly_100_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.gebco_poly_100_gid_seq OWNER TO postgres;

--
-- Name: gebco_poly_100_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.gebco_poly_100_gid_seq OWNED BY public.gebco_poly_100.gid;


--
-- Name: gebco_poly_2014; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.gebco_poly_2014 (
    gid integer NOT NULL,
    objectid integer,
    gridcode integer,
    shape_leng numeric,
    shape_area numeric,
    geom public.geometry,
    CONSTRAINT enforce_dims_geom CHECK ((public.st_ndims(geom) = 2)),
    CONSTRAINT enforce_geotype_geom CHECK (((public.geometrytype(geom) = 'MULTIPOLYGON'::text) OR (geom IS NULL))),
    CONSTRAINT enforce_srid_geom CHECK ((public.st_srid(geom) = 4326))
);


ALTER TABLE public.gebco_poly_2014 OWNER TO postgres;

--
-- Name: gebco_poly_2014_gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.gebco_poly_2014_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.gebco_poly_2014_gid_seq OWNER TO postgres;

--
-- Name: gebco_poly_2014_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.gebco_poly_2014_gid_seq OWNED BY public.gebco_poly_2014.gid;


--
-- Name: gebco_poly_final_gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.gebco_poly_final_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.gebco_poly_final_gid_seq OWNER TO postgres;

--
-- Name: gueltig; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.gueltig (
    gid integer NOT NULL,
    lat numeric,
    lon numeric,
    dbs numeric,
    the_geom public.geometry,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.st_ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((public.geometrytype(the_geom) = 'POINT'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.st_srid(the_geom) = 4326))
);


ALTER TABLE public.gueltig OWNER TO postgres;

--
-- Name: gueltig_gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.gueltig_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.gueltig_gid_seq OWNER TO postgres;

--
-- Name: gueltig_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.gueltig_gid_seq OWNED BY public.gueltig.gid;


--
-- Name: lat; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.lat (
    gid integer NOT NULL,
    lat numeric,
    lon numeric(9,6),
    dbs numeric,
    the_geom public.geometry,
    datasetid character varying(100) DEFAULT 'default'::character varying NOT NULL,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.st_ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((public.geometrytype(the_geom) = 'POINT'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.st_srid(the_geom) = 4326))
);


ALTER TABLE public.lat OWNER TO postgres;

--
-- Name: lat_gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.lat_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.lat_gid_seq OWNER TO postgres;

--
-- Name: lat_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.lat_gid_seq OWNED BY public.lat.gid;


--
-- Name: seq_mer; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.seq_mer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_mer OWNER TO postgres;

--
-- Name: mergerun; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.mergerun (
    id bigint DEFAULT nextval('public.seq_mer'::regclass) NOT NULL,
    started timestamp without time zone,
    finished timestamp without time zone
);


ALTER TABLE public.mergerun OWNER TO postgres;

SET default_with_oids = true;

--
-- Name: shallowwater; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.shallowwater (
    id integer NOT NULL
);


ALTER TABLE public.shallowwater OWNER TO postgres;

--
-- Name: ocean_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.ocean_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.ocean_id_seq OWNER TO postgres;

--
-- Name: ocean_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.ocean_id_seq OWNED BY public.shallowwater.id;


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

SET default_with_oids = false;

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
-- Name: sbassensor; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.sbassensor AS
 SELECT sbassensor.vesselconfigid,
    sbassensor.x,
    sbassensor.y,
    sbassensor.z,
    sbassensor.sensorid,
    sbassensor.manufacturer,
    sbassensor.model,
    sbassensor.id
   FROM osmapi_tables.sbassensor;


ALTER TABLE public.sbassensor OWNER TO postgres;

--
-- Name: seq_tmr; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.seq_tmr
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_tmr OWNER TO postgres;

--
-- Name: seq_tpr; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.seq_tpr
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_tpr OWNER TO postgres;

--
-- Name: split; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.split (
    gid integer NOT NULL,
    gridcode numeric(10,0),
    the_geom public.geometry,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.st_ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((public.geometrytype(the_geom) = 'MULTIPOLYGON'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.st_srid(the_geom) = 4326))
);


ALTER TABLE public.split OWNER TO postgres;

--
-- Name: split_gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.split_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.split_gid_seq OWNER TO postgres;

--
-- Name: split_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.split_gid_seq OWNED BY public.split.gid;


--
-- Name: test_zoom_10_cor_1_points; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.test_zoom_10_cor_1_points (
    gid integer NOT NULL,
    the_geom public.geometry
);


ALTER TABLE public.test_zoom_10_cor_1_points OWNER TO postgres;

--
-- Name: test_zoom_2_cor_1_points; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.test_zoom_2_cor_1_points (
    gid integer NOT NULL,
    the_geom public.geometry
);


ALTER TABLE public.test_zoom_2_cor_1_points OWNER TO postgres;

--
-- Name: test_zoom_3_cor_1_points; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.test_zoom_3_cor_1_points (
    gid integer NOT NULL,
    the_geom public.geometry
);


ALTER TABLE public.test_zoom_3_cor_1_points OWNER TO postgres;

--
-- Name: test_zoom_4_cor_1_points; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.test_zoom_4_cor_1_points (
    gid integer NOT NULL,
    the_geom public.geometry
);


ALTER TABLE public.test_zoom_4_cor_1_points OWNER TO postgres;

--
-- Name: test_zoom_5_cor_1_points; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.test_zoom_5_cor_1_points (
    gid integer NOT NULL,
    the_geom public.geometry
);


ALTER TABLE public.test_zoom_5_cor_1_points OWNER TO postgres;

--
-- Name: test_zoom_6_cor_1_points; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.test_zoom_6_cor_1_points (
    gid integer NOT NULL,
    the_geom public.geometry
);


ALTER TABLE public.test_zoom_6_cor_1_points OWNER TO postgres;

--
-- Name: test_zoom_7_cor_1_points; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.test_zoom_7_cor_1_points (
    gid integer NOT NULL,
    the_geom public.geometry
);


ALTER TABLE public.test_zoom_7_cor_1_points OWNER TO postgres;

--
-- Name: test_zoom_8_cor_1_points; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.test_zoom_8_cor_1_points (
    gid integer NOT NULL,
    the_geom public.geometry
);


ALTER TABLE public.test_zoom_8_cor_1_points OWNER TO postgres;

--
-- Name: test_zoom_9_cor_1_points; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.test_zoom_9_cor_1_points (
    gid integer NOT NULL,
    the_geom public.geometry
);


ALTER TABLE public.test_zoom_9_cor_1_points OWNER TO postgres;

--
-- Name: track_info; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.track_info AS
 SELECT track_info.id,
    track_info.tra_id,
    track_info.short_info,
    track_info.long_info,
    track_info.reprocess,
    track_info.discard,
    track_info.ignore
   FROM osmapi_tables.track_info;


ALTER TABLE public.track_info OWNER TO postgres;

--
-- Name: track_mergerun; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.track_mergerun (
    id bigint DEFAULT nextval('public.seq_tmr'::regclass) NOT NULL,
    tra_id bigint NOT NULL,
    mer_id bigint NOT NULL
);


ALTER TABLE public.track_mergerun OWNER TO postgres;

--
-- Name: trackpoints; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints (
    gid integer NOT NULL,
    objectid integer,
    the_geom public.geometry,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.st_ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((public.geometrytype(the_geom) = 'POINT'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.st_srid(the_geom) = 4326))
);


ALTER TABLE public.trackpoints OWNER TO postgres;

--
-- Name: trackpoints_cor1; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints_cor1 (
    gid integer NOT NULL,
    lat numeric,
    lon numeric,
    dbs numeric,
    the_geom public.geometry,
    datasetid character varying(100) DEFAULT 'default'::character varying NOT NULL,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.st_ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((public.geometrytype(the_geom) = 'POINT'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.st_srid(the_geom) = 4326))
);


ALTER TABLE public.trackpoints_cor1 OWNER TO postgres;

--
-- Name: trackpoints_cor1_gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.trackpoints_cor1_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.trackpoints_cor1_gid_seq OWNER TO postgres;

--
-- Name: trackpoints_cor1_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.trackpoints_cor1_gid_seq OWNED BY public.trackpoints_cor1.gid;


--
-- Name: trackpoints_cor_1; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints_cor_1 (
    gid integer NOT NULL,
    lat numeric,
    lon numeric,
    dbs numeric,
    the_geom public.geometry,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.st_ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((public.geometrytype(the_geom) = 'POINT'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.st_srid(the_geom) = 4326))
);


ALTER TABLE public.trackpoints_cor_1 OWNER TO postgres;

--
-- Name: trackpoints_cor_1_gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.trackpoints_cor_1_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.trackpoints_cor_1_gid_seq OWNER TO postgres;

--
-- Name: trackpoints_cor_1_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.trackpoints_cor_1_gid_seq OWNED BY public.trackpoints_cor_1.gid;


--
-- Name: trackpoints_cortest; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints_cortest (
    lat numeric,
    lon numeric(11,8),
    dbs numeric(8,2),
    the_geom public.geometry,
    datasetid character varying(100),
    valid boolean,
    gid integer NOT NULL,
    CONSTRAINT trackpoints_cortest_the_geom_check CHECK ((public.st_ndims(the_geom) = 2)),
    CONSTRAINT trackpoints_cortest_the_geom_check1 CHECK (((public.geometrytype(the_geom) = 'POINT'::text) OR (the_geom IS NULL))),
    CONSTRAINT trackpoints_cortest_the_geom_check2 CHECK ((public.st_srid(the_geom) = 4326))
);


ALTER TABLE public.trackpoints_cortest OWNER TO postgres;

--
-- Name: trackpoints_cortest_gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.trackpoints_cortest_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.trackpoints_cortest_gid_seq OWNER TO postgres;

--
-- Name: trackpoints_cortest_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.trackpoints_cortest_gid_seq OWNED BY public.trackpoints_cortest.gid;


--
-- Name: trackpoints_empty; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints_empty (
    lat numeric,
    lon numeric(11,8),
    dbs numeric(8,2),
    valid boolean,
    gid integer NOT NULL,
    datasetid integer,
    the_geom public.geometry,
    recordingdate timestamp without time zone,
    latvar double precision DEFAULT 0,
    lonvar double precision DEFAULT 0,
    depthvar double precision DEFAULT 0,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.st_ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((public.geometrytype(the_geom) = 'POINT'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.st_srid(the_geom) = 4326))
);


ALTER TABLE public.trackpoints_empty OWNER TO postgres;

--
-- Name: trackpoints_empty_gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.trackpoints_empty_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.trackpoints_empty_gid_seq OWNER TO postgres;

--
-- Name: trackpoints_empty_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.trackpoints_empty_gid_seq OWNED BY public.trackpoints_empty.gid;


--
-- Name: trackpoints_gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.trackpoints_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.trackpoints_gid_seq OWNER TO postgres;

--
-- Name: trackpoints_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.trackpoints_gid_seq OWNED BY public.trackpoints.gid;


--
-- Name: trackpoints_raw_10; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints_raw_10 (
    lat numeric,
    lon numeric(11,8),
    dbs numeric(8,2),
    valid boolean,
    gid integer NOT NULL,
    datasetid integer,
    the_geom public.geometry,
    recordingdate timestamp without time zone,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.st_ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((public.geometrytype(the_geom) = 'POINT'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.st_srid(the_geom) = 4326))
);


ALTER TABLE public.trackpoints_raw_10 OWNER TO postgres;

--
-- Name: trackpoints_raw_10_gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.trackpoints_raw_10_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.trackpoints_raw_10_gid_seq OWNER TO postgres;

--
-- Name: trackpoints_raw_10_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.trackpoints_raw_10_gid_seq OWNED BY public.trackpoints_raw_10.gid;


--
-- Name: trackpoints_raw_12; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints_raw_12 (
    lat numeric,
    lon numeric(11,8),
    dbs numeric(8,2),
    valid boolean,
    gid integer NOT NULL,
    datasetid integer,
    the_geom public.geometry,
    recordingdate timestamp without time zone,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.st_ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((public.geometrytype(the_geom) = 'POINT'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.st_srid(the_geom) = 4326))
);


ALTER TABLE public.trackpoints_raw_12 OWNER TO postgres;

--
-- Name: trackpoints_raw_12_gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.trackpoints_raw_12_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.trackpoints_raw_12_gid_seq OWNER TO postgres;

--
-- Name: trackpoints_raw_12_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.trackpoints_raw_12_gid_seq OWNED BY public.trackpoints_raw_12.gid;


--
-- Name: trackpoints_raw_16; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints_raw_16 (
    lat numeric,
    lon numeric(11,8),
    dbs numeric(8,2),
    the_geom public.geometry,
    valid boolean,
    gid integer NOT NULL,
    datasetid integer,
    recordingdate timestamp without time zone,
    latvar double precision DEFAULT 0,
    lonvar double precision DEFAULT 0,
    depthvar double precision DEFAULT 0,
    CONSTRAINT trackpoints_raw_16_the_geom_check CHECK ((public.st_ndims(the_geom) = 2)),
    CONSTRAINT trackpoints_raw_16_the_geom_check1 CHECK (((public.geometrytype(the_geom) = 'POINT'::text) OR (the_geom IS NULL))),
    CONSTRAINT trackpoints_raw_16_the_geom_check2 CHECK ((public.st_srid(the_geom) = 4326))
);


ALTER TABLE public.trackpoints_raw_16 OWNER TO postgres;

--
-- Name: trackpoints_raw_16_gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.trackpoints_raw_16_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.trackpoints_raw_16_gid_seq OWNER TO postgres;

--
-- Name: trackpoints_raw_16_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.trackpoints_raw_16_gid_seq OWNED BY public.trackpoints_raw_16.gid;


--
-- Name: trackpoints_raw_8; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints_raw_8 (
    lat numeric,
    lon numeric(11,8),
    dbs numeric(8,2),
    the_geom public.geometry,
    valid boolean,
    gid integer NOT NULL,
    datasetid integer,
    recordingdate timestamp without time zone,
    CONSTRAINT trackpoints_raw_8_the_geom_check CHECK ((public.st_ndims(the_geom) = 2)),
    CONSTRAINT trackpoints_raw_8_the_geom_check1 CHECK (((public.geometrytype(the_geom) = 'POINT'::text) OR (the_geom IS NULL))),
    CONSTRAINT trackpoints_raw_8_the_geom_check2 CHECK ((public.st_srid(the_geom) = 4326))
);


ALTER TABLE public.trackpoints_raw_8 OWNER TO postgres;

--
-- Name: trackpoints_raw_8_gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.trackpoints_raw_8_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.trackpoints_raw_8_gid_seq OWNER TO postgres;

--
-- Name: trackpoints_raw_8_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.trackpoints_raw_8_gid_seq OWNED BY public.trackpoints_raw_8.gid;


--
-- Name: trackpoints_raw_filter_10; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints_raw_filter_10 (
    lat numeric,
    lon numeric(11,8),
    dbs numeric(8,2),
    valid boolean,
    gid integer NOT NULL,
    datasetid integer,
    the_geom public.geometry,
    recordingdate timestamp with time zone,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.st_ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((public.geometrytype(the_geom) = 'POINT'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.st_srid(the_geom) = 4326))
);


ALTER TABLE public.trackpoints_raw_filter_10 OWNER TO postgres;

--
-- Name: trackpoints_raw_filter_10_gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.trackpoints_raw_filter_10_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.trackpoints_raw_filter_10_gid_seq OWNER TO postgres;

--
-- Name: trackpoints_raw_filter_10_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.trackpoints_raw_filter_10_gid_seq OWNED BY public.trackpoints_raw_filter_10.gid;


--
-- Name: trackpoints_raw_filter_12; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints_raw_filter_12 (
    lat numeric,
    lon numeric(11,8),
    dbs numeric(8,2),
    valid boolean,
    gid integer NOT NULL,
    datasetid integer,
    the_geom public.geometry,
    recordingdate timestamp with time zone,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.st_ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((public.geometrytype(the_geom) = 'POINT'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.st_srid(the_geom) = 4326))
);


ALTER TABLE public.trackpoints_raw_filter_12 OWNER TO postgres;

--
-- Name: trackpoints_raw_filter_12_gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.trackpoints_raw_filter_12_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.trackpoints_raw_filter_12_gid_seq OWNER TO postgres;

--
-- Name: trackpoints_raw_filter_12_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.trackpoints_raw_filter_12_gid_seq OWNED BY public.trackpoints_raw_filter_12.gid;


--
-- Name: trackpoints_raw_filter_16; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints_raw_filter_16 (
    lat numeric,
    lon numeric(11,8),
    dbs numeric(8,2),
    valid boolean,
    gid integer NOT NULL,
    datasetid integer,
    the_geom public.geometry,
    recordingdate timestamp without time zone,
    latvar double precision DEFAULT 0,
    lonvar double precision DEFAULT 0,
    depthvar double precision DEFAULT 0,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.st_ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((public.geometrytype(the_geom) = 'POINT'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.st_srid(the_geom) = 4326))
);


ALTER TABLE public.trackpoints_raw_filter_16 OWNER TO postgres;

--
-- Name: trackpoints_raw_filter_16_gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.trackpoints_raw_filter_16_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.trackpoints_raw_filter_16_gid_seq OWNER TO postgres;

--
-- Name: trackpoints_raw_filter_16_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.trackpoints_raw_filter_16_gid_seq OWNED BY public.trackpoints_raw_filter_16.gid;


--
-- Name: trackpoints_raw_filter_8; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints_raw_filter_8 (
    lat numeric,
    lon numeric(11,8),
    dbs numeric(8,2),
    valid boolean,
    gid integer NOT NULL,
    datasetid integer,
    the_geom public.geometry,
    recordingdate timestamp with time zone,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.st_ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((public.geometrytype(the_geom) = 'POINT'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.st_srid(the_geom) = 4326))
);


ALTER TABLE public.trackpoints_raw_filter_8 OWNER TO postgres;

--
-- Name: trackpoints_raw_filter_8_gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.trackpoints_raw_filter_8_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.trackpoints_raw_filter_8_gid_seq OWNER TO postgres;

--
-- Name: trackpoints_raw_filter_8_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.trackpoints_raw_filter_8_gid_seq OWNED BY public.trackpoints_raw_filter_8.gid;


--
-- Name: trackpoints_raw_merge_1; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints_raw_merge_1 (
    gid bigint DEFAULT nextval('public.seq_tpr'::regclass) NOT NULL,
    dbs double precision,
    the_geom public.geometry,
    lat double precision,
    lon double precision,
    num_points bigint,
    call_id bigint,
    gids character varying
);


ALTER TABLE public.trackpoints_raw_merge_1 OWNER TO postgres;

--
-- Name: trackpoints_raw_merge_10; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints_raw_merge_10 (
    gid bigint DEFAULT nextval('public.seq_tpr'::regclass) NOT NULL,
    dbs double precision,
    the_geom public.geometry,
    lat double precision,
    lon double precision,
    num_points bigint,
    call_id bigint,
    gids character varying
);


ALTER TABLE public.trackpoints_raw_merge_10 OWNER TO postgres;

--
-- Name: trackpoints_raw_merge_11; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints_raw_merge_11 (
    gid bigint DEFAULT nextval('public.seq_tpr'::regclass) NOT NULL,
    dbs double precision,
    the_geom public.geometry,
    lat double precision,
    lon double precision,
    num_points bigint,
    call_id bigint,
    gids character varying
);


ALTER TABLE public.trackpoints_raw_merge_11 OWNER TO postgres;

--
-- Name: trackpoints_raw_merge_12; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints_raw_merge_12 (
    gid bigint DEFAULT nextval('public.seq_tpr'::regclass) NOT NULL,
    dbs double precision,
    the_geom public.geometry,
    lat double precision,
    lon double precision,
    num_points bigint,
    call_id bigint,
    gids character varying
);


ALTER TABLE public.trackpoints_raw_merge_12 OWNER TO postgres;

--
-- Name: trackpoints_raw_merge_13; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints_raw_merge_13 (
    gid bigint DEFAULT nextval('public.seq_tpr'::regclass) NOT NULL,
    dbs double precision,
    the_geom public.geometry,
    lat double precision,
    lon double precision,
    num_points bigint,
    call_id bigint,
    gids character varying
);


ALTER TABLE public.trackpoints_raw_merge_13 OWNER TO postgres;

--
-- Name: trackpoints_raw_merge_14; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints_raw_merge_14 (
    gid bigint DEFAULT nextval('public.seq_tpr'::regclass) NOT NULL,
    dbs double precision,
    the_geom public.geometry,
    lat double precision,
    lon double precision,
    num_points bigint,
    call_id bigint,
    gids character varying
);


ALTER TABLE public.trackpoints_raw_merge_14 OWNER TO postgres;

--
-- Name: trackpoints_raw_merge_15; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints_raw_merge_15 (
    gid bigint DEFAULT nextval('public.seq_tpr'::regclass) NOT NULL,
    dbs double precision,
    the_geom public.geometry,
    lat double precision,
    lon double precision,
    num_points bigint,
    call_id bigint,
    gids character varying
);


ALTER TABLE public.trackpoints_raw_merge_15 OWNER TO postgres;

--
-- Name: trackpoints_raw_merge_16; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints_raw_merge_16 (
    gid bigint DEFAULT nextval('public.seq_tpr'::regclass) NOT NULL,
    dbs double precision,
    the_geom public.geometry,
    lat double precision,
    lon double precision,
    num_points bigint,
    call_id bigint,
    gids character varying
);


ALTER TABLE public.trackpoints_raw_merge_16 OWNER TO postgres;

--
-- Name: trackpoints_raw_merge_17; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints_raw_merge_17 (
    gid bigint DEFAULT nextval('public.seq_tpr'::regclass) NOT NULL,
    dbs double precision,
    the_geom public.geometry,
    lat double precision,
    lon double precision,
    num_points bigint,
    call_id bigint,
    gids character varying
);


ALTER TABLE public.trackpoints_raw_merge_17 OWNER TO postgres;

--
-- Name: trackpoints_raw_merge_18; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints_raw_merge_18 (
    gid bigint DEFAULT nextval('public.seq_tpr'::regclass) NOT NULL,
    dbs double precision,
    the_geom public.geometry,
    lat double precision,
    lon double precision,
    num_points bigint,
    call_id bigint,
    gids character varying
);


ALTER TABLE public.trackpoints_raw_merge_18 OWNER TO postgres;

--
-- Name: trackpoints_raw_merge_19; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints_raw_merge_19 (
    gid bigint DEFAULT nextval('public.seq_tpr'::regclass) NOT NULL,
    dbs double precision,
    the_geom public.geometry,
    lat double precision,
    lon double precision,
    num_points bigint,
    call_id bigint,
    gids character varying
);


ALTER TABLE public.trackpoints_raw_merge_19 OWNER TO postgres;

--
-- Name: trackpoints_raw_merge_2; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints_raw_merge_2 (
    gid bigint DEFAULT nextval('public.seq_tpr'::regclass) NOT NULL,
    dbs double precision,
    the_geom public.geometry,
    lat double precision,
    lon double precision,
    num_points bigint,
    call_id bigint,
    gids character varying
);


ALTER TABLE public.trackpoints_raw_merge_2 OWNER TO postgres;

--
-- Name: trackpoints_raw_merge_20; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints_raw_merge_20 (
    gid bigint DEFAULT nextval('public.seq_tpr'::regclass) NOT NULL,
    dbs double precision,
    the_geom public.geometry,
    lat double precision,
    lon double precision,
    num_points bigint,
    call_id bigint,
    gids character varying
);


ALTER TABLE public.trackpoints_raw_merge_20 OWNER TO postgres;

--
-- Name: trackpoints_raw_merge_21; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints_raw_merge_21 (
    gid bigint DEFAULT nextval('public.seq_tpr'::regclass) NOT NULL,
    dbs double precision,
    the_geom public.geometry,
    lat double precision,
    lon double precision,
    num_points bigint,
    call_id bigint,
    gids character varying
);


ALTER TABLE public.trackpoints_raw_merge_21 OWNER TO postgres;

--
-- Name: trackpoints_raw_merge_22; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints_raw_merge_22 (
    gid bigint DEFAULT nextval('public.seq_tpr'::regclass) NOT NULL,
    dbs double precision,
    the_geom public.geometry,
    lat double precision,
    lon double precision,
    num_points bigint,
    call_id bigint,
    gids character varying
);


ALTER TABLE public.trackpoints_raw_merge_22 OWNER TO postgres;

--
-- Name: trackpoints_raw_merge_3; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints_raw_merge_3 (
    gid bigint DEFAULT nextval('public.seq_tpr'::regclass) NOT NULL,
    dbs double precision,
    the_geom public.geometry,
    lat double precision,
    lon double precision,
    num_points bigint,
    call_id bigint,
    gids character varying
);


ALTER TABLE public.trackpoints_raw_merge_3 OWNER TO postgres;

--
-- Name: trackpoints_raw_merge_4; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints_raw_merge_4 (
    gid bigint DEFAULT nextval('public.seq_tpr'::regclass) NOT NULL,
    dbs double precision,
    the_geom public.geometry,
    lat double precision,
    lon double precision,
    num_points bigint,
    call_id bigint,
    gids character varying
);


ALTER TABLE public.trackpoints_raw_merge_4 OWNER TO postgres;

--
-- Name: trackpoints_raw_merge_5; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints_raw_merge_5 (
    gid bigint DEFAULT nextval('public.seq_tpr'::regclass) NOT NULL,
    dbs double precision,
    the_geom public.geometry,
    lat double precision,
    lon double precision,
    num_points bigint,
    call_id bigint,
    gids character varying
);


ALTER TABLE public.trackpoints_raw_merge_5 OWNER TO postgres;

--
-- Name: trackpoints_raw_merge_6; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints_raw_merge_6 (
    gid bigint DEFAULT nextval('public.seq_tpr'::regclass) NOT NULL,
    dbs double precision,
    the_geom public.geometry,
    lat double precision,
    lon double precision,
    num_points bigint,
    call_id bigint,
    gids character varying
);


ALTER TABLE public.trackpoints_raw_merge_6 OWNER TO postgres;

--
-- Name: trackpoints_raw_merge_7; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints_raw_merge_7 (
    gid bigint DEFAULT nextval('public.seq_tpr'::regclass) NOT NULL,
    dbs double precision,
    the_geom public.geometry,
    lat double precision,
    lon double precision,
    num_points bigint,
    call_id bigint,
    gids character varying
);


ALTER TABLE public.trackpoints_raw_merge_7 OWNER TO postgres;

--
-- Name: trackpoints_raw_merge_8; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints_raw_merge_8 (
    gid bigint DEFAULT nextval('public.seq_tpr'::regclass) NOT NULL,
    dbs double precision,
    the_geom public.geometry,
    lat double precision,
    lon double precision,
    num_points bigint,
    call_id bigint,
    gids character varying
);


ALTER TABLE public.trackpoints_raw_merge_8 OWNER TO postgres;

--
-- Name: trackpoints_raw_merge_9; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints_raw_merge_9 (
    gid bigint DEFAULT nextval('public.seq_tpr'::regclass) NOT NULL,
    dbs double precision,
    the_geom public.geometry,
    lat double precision,
    lon double precision,
    num_points bigint,
    call_id bigint,
    gids character varying
);


ALTER TABLE public.trackpoints_raw_merge_9 OWNER TO postgres;

--
-- Name: trackpoints_raw_render_1; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints_raw_render_1 (
    gid bigint NOT NULL,
    track_id bigint,
    dbs numeric(8,2),
    the_geom public.geometry
);


ALTER TABLE public.trackpoints_raw_render_1 OWNER TO postgres;

--
-- Name: trackpoints_raw_render_10; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints_raw_render_10 (
    gid bigint NOT NULL,
    track_id bigint,
    dbs numeric(8,2),
    the_geom public.geometry
);


ALTER TABLE public.trackpoints_raw_render_10 OWNER TO postgres;

--
-- Name: trackpoints_raw_render_11; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints_raw_render_11 (
    gid bigint NOT NULL,
    track_id bigint,
    dbs numeric(8,2),
    the_geom public.geometry
);


ALTER TABLE public.trackpoints_raw_render_11 OWNER TO postgres;

--
-- Name: trackpoints_raw_render_12; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints_raw_render_12 (
    gid bigint NOT NULL,
    track_id bigint,
    dbs numeric(8,2),
    the_geom public.geometry
);


ALTER TABLE public.trackpoints_raw_render_12 OWNER TO postgres;

--
-- Name: trackpoints_raw_render_13; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints_raw_render_13 (
    gid bigint NOT NULL,
    track_id bigint,
    dbs numeric(8,2),
    the_geom public.geometry
);


ALTER TABLE public.trackpoints_raw_render_13 OWNER TO postgres;

--
-- Name: trackpoints_raw_render_14; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints_raw_render_14 (
    gid bigint NOT NULL,
    track_id bigint,
    dbs numeric(8,2),
    the_geom public.geometry
);


ALTER TABLE public.trackpoints_raw_render_14 OWNER TO postgres;

--
-- Name: trackpoints_raw_render_15; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints_raw_render_15 (
    gid bigint NOT NULL,
    track_id bigint,
    dbs numeric(8,2),
    the_geom public.geometry
);


ALTER TABLE public.trackpoints_raw_render_15 OWNER TO postgres;

--
-- Name: trackpoints_raw_render_16; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints_raw_render_16 (
    gid bigint NOT NULL,
    track_id bigint,
    dbs numeric(8,2),
    the_geom public.geometry
);


ALTER TABLE public.trackpoints_raw_render_16 OWNER TO postgres;

--
-- Name: trackpoints_raw_render_17; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints_raw_render_17 (
    gid bigint NOT NULL,
    track_id bigint,
    dbs numeric(8,2),
    the_geom public.geometry
);


ALTER TABLE public.trackpoints_raw_render_17 OWNER TO postgres;

--
-- Name: trackpoints_raw_render_18; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints_raw_render_18 (
    gid bigint NOT NULL,
    track_id bigint,
    dbs numeric(8,2),
    the_geom public.geometry
);


ALTER TABLE public.trackpoints_raw_render_18 OWNER TO postgres;

--
-- Name: trackpoints_raw_render_19; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints_raw_render_19 (
    gid bigint NOT NULL,
    track_id bigint,
    dbs numeric(8,2),
    the_geom public.geometry
);


ALTER TABLE public.trackpoints_raw_render_19 OWNER TO postgres;

--
-- Name: trackpoints_raw_render_2; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints_raw_render_2 (
    gid bigint NOT NULL,
    track_id bigint,
    dbs numeric(8,2),
    the_geom public.geometry
);


ALTER TABLE public.trackpoints_raw_render_2 OWNER TO postgres;

--
-- Name: trackpoints_raw_render_20; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints_raw_render_20 (
    gid bigint NOT NULL,
    track_id bigint,
    dbs numeric(8,2),
    the_geom public.geometry
);


ALTER TABLE public.trackpoints_raw_render_20 OWNER TO postgres;

--
-- Name: trackpoints_raw_render_21; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints_raw_render_21 (
    gid bigint NOT NULL,
    track_id bigint,
    dbs numeric(8,2),
    the_geom public.geometry
);


ALTER TABLE public.trackpoints_raw_render_21 OWNER TO postgres;

--
-- Name: trackpoints_raw_render_22; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints_raw_render_22 (
    gid bigint NOT NULL,
    track_id bigint,
    dbs numeric(8,2),
    the_geom public.geometry
);


ALTER TABLE public.trackpoints_raw_render_22 OWNER TO postgres;

--
-- Name: trackpoints_raw_render_3; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints_raw_render_3 (
    gid bigint NOT NULL,
    track_id bigint,
    dbs numeric(8,2),
    the_geom public.geometry
);


ALTER TABLE public.trackpoints_raw_render_3 OWNER TO postgres;

--
-- Name: trackpoints_raw_render_4; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints_raw_render_4 (
    gid bigint NOT NULL,
    track_id bigint,
    dbs numeric(8,2),
    the_geom public.geometry
);


ALTER TABLE public.trackpoints_raw_render_4 OWNER TO postgres;

--
-- Name: trackpoints_raw_render_5; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints_raw_render_5 (
    gid bigint NOT NULL,
    track_id bigint,
    dbs numeric(8,2),
    the_geom public.geometry
);


ALTER TABLE public.trackpoints_raw_render_5 OWNER TO postgres;

--
-- Name: trackpoints_raw_render_6; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints_raw_render_6 (
    gid bigint NOT NULL,
    track_id bigint,
    dbs numeric(8,2),
    the_geom public.geometry
);


ALTER TABLE public.trackpoints_raw_render_6 OWNER TO postgres;

--
-- Name: trackpoints_raw_render_7; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints_raw_render_7 (
    gid bigint NOT NULL,
    track_id bigint,
    dbs numeric(8,2),
    the_geom public.geometry
);


ALTER TABLE public.trackpoints_raw_render_7 OWNER TO postgres;

--
-- Name: trackpoints_raw_render_8; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints_raw_render_8 (
    gid bigint NOT NULL,
    track_id bigint,
    dbs numeric(8,2),
    the_geom public.geometry
);


ALTER TABLE public.trackpoints_raw_render_8 OWNER TO postgres;

--
-- Name: trackpoints_raw_render_9; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints_raw_render_9 (
    gid bigint NOT NULL,
    track_id bigint,
    dbs numeric(8,2),
    the_geom public.geometry
);


ALTER TABLE public.trackpoints_raw_render_9 OWNER TO postgres;

--
-- Name: trackpoints_raw_temp_10; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints_raw_temp_10 (
    lat numeric,
    lon numeric(11,8),
    dbs numeric(8,2),
    valid boolean,
    gid integer NOT NULL,
    datasetid integer,
    the_geom public.geometry,
    recordingdate timestamp with time zone,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.st_ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((public.geometrytype(the_geom) = 'POINT'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.st_srid(the_geom) = 4326))
);


ALTER TABLE public.trackpoints_raw_temp_10 OWNER TO postgres;

--
-- Name: trackpoints_raw_temp_10_gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.trackpoints_raw_temp_10_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.trackpoints_raw_temp_10_gid_seq OWNER TO postgres;

--
-- Name: trackpoints_raw_temp_10_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.trackpoints_raw_temp_10_gid_seq OWNED BY public.trackpoints_raw_temp_10.gid;


--
-- Name: trackpoints_raw_temp_12; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints_raw_temp_12 (
    lat numeric,
    lon numeric(11,8),
    dbs numeric(8,2),
    valid boolean,
    gid integer NOT NULL,
    datasetid integer,
    the_geom public.geometry,
    recordingdate timestamp with time zone,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.st_ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((public.geometrytype(the_geom) = 'POINT'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.st_srid(the_geom) = 4326))
);


ALTER TABLE public.trackpoints_raw_temp_12 OWNER TO postgres;

--
-- Name: trackpoints_raw_temp_12_gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.trackpoints_raw_temp_12_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.trackpoints_raw_temp_12_gid_seq OWNER TO postgres;

--
-- Name: trackpoints_raw_temp_12_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.trackpoints_raw_temp_12_gid_seq OWNED BY public.trackpoints_raw_temp_12.gid;


--
-- Name: trackpoints_raw_temp_16; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints_raw_temp_16 (
    lat numeric,
    lon numeric(11,8),
    dbs numeric(8,2),
    valid boolean,
    gid integer NOT NULL,
    datasetid integer,
    the_geom public.geometry,
    recordingdate timestamp without time zone,
    latvar double precision DEFAULT 0,
    lonvar double precision DEFAULT 0,
    depthvar double precision DEFAULT 0,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.st_ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((public.geometrytype(the_geom) = 'POINT'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.st_srid(the_geom) = 4326))
);


ALTER TABLE public.trackpoints_raw_temp_16 OWNER TO postgres;

--
-- Name: trackpoints_raw_temp_16_gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.trackpoints_raw_temp_16_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.trackpoints_raw_temp_16_gid_seq OWNER TO postgres;

--
-- Name: trackpoints_raw_temp_16_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.trackpoints_raw_temp_16_gid_seq OWNED BY public.trackpoints_raw_temp_16.gid;


--
-- Name: trackpoints_raw_temp_8; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints_raw_temp_8 (
    lat numeric,
    lon numeric(11,8),
    dbs numeric(8,2),
    valid boolean,
    gid integer NOT NULL,
    datasetid integer,
    the_geom public.geometry,
    recordingdate timestamp with time zone,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.st_ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((public.geometrytype(the_geom) = 'POINT'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.st_srid(the_geom) = 4326))
);


ALTER TABLE public.trackpoints_raw_temp_8 OWNER TO postgres;

--
-- Name: trackpoints_raw_temp_8_gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.trackpoints_raw_temp_8_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.trackpoints_raw_temp_8_gid_seq OWNER TO postgres;

--
-- Name: trackpoints_raw_temp_8_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.trackpoints_raw_temp_8_gid_seq OWNED BY public.trackpoints_raw_temp_8.gid;


--
-- Name: trackpoints_test1_10; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints_test1_10 (
    lat numeric,
    lon numeric(11,8),
    dbs numeric(8,2),
    valid boolean,
    gid integer NOT NULL,
    datasetid integer,
    the_geom public.geometry,
    recordingdate timestamp with time zone,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.st_ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((public.geometrytype(the_geom) = 'POINT'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.st_srid(the_geom) = 4326))
);


ALTER TABLE public.trackpoints_test1_10 OWNER TO postgres;

--
-- Name: trackpoints_test1_10_gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.trackpoints_test1_10_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.trackpoints_test1_10_gid_seq OWNER TO postgres;

--
-- Name: trackpoints_test1_10_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.trackpoints_test1_10_gid_seq OWNED BY public.trackpoints_test1_10.gid;


--
-- Name: trackpoints_test1_12; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints_test1_12 (
    lat numeric,
    lon numeric(11,8),
    dbs numeric(8,2),
    valid boolean,
    gid integer NOT NULL,
    datasetid integer,
    the_geom public.geometry,
    recordingdate timestamp with time zone,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.st_ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((public.geometrytype(the_geom) = 'POINT'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.st_srid(the_geom) = 4326))
);


ALTER TABLE public.trackpoints_test1_12 OWNER TO postgres;

--
-- Name: trackpoints_test1_12_gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.trackpoints_test1_12_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.trackpoints_test1_12_gid_seq OWNER TO postgres;

--
-- Name: trackpoints_test1_12_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.trackpoints_test1_12_gid_seq OWNED BY public.trackpoints_test1_12.gid;


--
-- Name: trackpoints_test1_16; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints_test1_16 (
    lat numeric,
    lon numeric(11,8),
    dbs numeric(8,2),
    valid boolean,
    gid integer NOT NULL,
    datasetid integer,
    the_geom public.geometry,
    latvar double precision DEFAULT 0,
    lonvar double precision DEFAULT 0,
    depthvar double precision DEFAULT 0,
    recordingdate timestamp with time zone,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.st_ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((public.geometrytype(the_geom) = 'POINT'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.st_srid(the_geom) = 4326))
);


ALTER TABLE public.trackpoints_test1_16 OWNER TO postgres;

--
-- Name: trackpoints_test1_16_gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.trackpoints_test1_16_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.trackpoints_test1_16_gid_seq OWNER TO postgres;

--
-- Name: trackpoints_test1_16_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.trackpoints_test1_16_gid_seq OWNED BY public.trackpoints_test1_16.gid;


--
-- Name: trackpoints_test1_8; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints_test1_8 (
    lat numeric,
    lon numeric(11,8),
    dbs numeric(8,2),
    valid boolean,
    gid integer NOT NULL,
    datasetid integer,
    the_geom public.geometry,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.st_ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((public.geometrytype(the_geom) = 'POINT'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.st_srid(the_geom) = 4326))
);


ALTER TABLE public.trackpoints_test1_8 OWNER TO postgres;

--
-- Name: trackpoints_test1_8_gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.trackpoints_test1_8_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.trackpoints_test1_8_gid_seq OWNER TO postgres;

--
-- Name: trackpoints_test1_8_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.trackpoints_test1_8_gid_seq OWNED BY public.trackpoints_test1_8.gid;


--
-- Name: trackpoints_test2_10; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints_test2_10 (
    lat numeric,
    lon numeric(11,8),
    dbs numeric(8,2),
    valid boolean,
    gid integer NOT NULL,
    datasetid integer,
    the_geom public.geometry,
    recordingdate timestamp with time zone,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.st_ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((public.geometrytype(the_geom) = 'POINT'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.st_srid(the_geom) = 4326))
);


ALTER TABLE public.trackpoints_test2_10 OWNER TO postgres;

--
-- Name: trackpoints_test2_10_gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.trackpoints_test2_10_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.trackpoints_test2_10_gid_seq OWNER TO postgres;

--
-- Name: trackpoints_test2_10_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.trackpoints_test2_10_gid_seq OWNED BY public.trackpoints_test2_10.gid;


--
-- Name: trackpoints_test2_12; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints_test2_12 (
    lat numeric,
    lon numeric(11,8),
    dbs numeric(8,2),
    valid boolean,
    gid integer NOT NULL,
    datasetid integer,
    the_geom public.geometry,
    recordingdate timestamp with time zone,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.st_ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((public.geometrytype(the_geom) = 'POINT'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.st_srid(the_geom) = 4326))
);


ALTER TABLE public.trackpoints_test2_12 OWNER TO postgres;

--
-- Name: trackpoints_test2_12_gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.trackpoints_test2_12_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.trackpoints_test2_12_gid_seq OWNER TO postgres;

--
-- Name: trackpoints_test2_12_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.trackpoints_test2_12_gid_seq OWNED BY public.trackpoints_test2_12.gid;


--
-- Name: trackpoints_test2_16; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints_test2_16 (
    lat numeric,
    lon numeric(11,8),
    dbs numeric(8,2),
    valid boolean,
    gid integer NOT NULL,
    datasetid integer,
    the_geom public.geometry,
    latvar double precision DEFAULT 0,
    lonvar double precision DEFAULT 0,
    depthvar double precision DEFAULT 0,
    recordingdate timestamp with time zone,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.st_ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((public.geometrytype(the_geom) = 'POINT'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.st_srid(the_geom) = 4326))
);


ALTER TABLE public.trackpoints_test2_16 OWNER TO postgres;

--
-- Name: trackpoints_test2_16_gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.trackpoints_test2_16_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.trackpoints_test2_16_gid_seq OWNER TO postgres;

--
-- Name: trackpoints_test2_16_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.trackpoints_test2_16_gid_seq OWNED BY public.trackpoints_test2_16.gid;


--
-- Name: trackpoints_test2_8; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints_test2_8 (
    lat numeric,
    lon numeric(11,8),
    dbs numeric(8,2),
    valid boolean,
    gid integer NOT NULL,
    datasetid integer,
    the_geom public.geometry,
    recordingdate timestamp with time zone,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.st_ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((public.geometrytype(the_geom) = 'POINT'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.st_srid(the_geom) = 4326))
);


ALTER TABLE public.trackpoints_test2_8 OWNER TO postgres;

--
-- Name: trackpoints_test2_8_gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.trackpoints_test2_8_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.trackpoints_test2_8_gid_seq OWNER TO postgres;

--
-- Name: trackpoints_test2_8_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.trackpoints_test2_8_gid_seq OWNED BY public.trackpoints_test2_8.gid;


--
-- Name: trackpoints_test3_10; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints_test3_10 (
    lat numeric,
    lon numeric(11,8),
    dbs numeric(8,2),
    valid boolean,
    gid integer NOT NULL,
    datasetid integer,
    the_geom public.geometry,
    recordingdate timestamp with time zone,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.st_ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((public.geometrytype(the_geom) = 'POINT'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.st_srid(the_geom) = 4326))
);


ALTER TABLE public.trackpoints_test3_10 OWNER TO postgres;

--
-- Name: trackpoints_test3_10_gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.trackpoints_test3_10_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.trackpoints_test3_10_gid_seq OWNER TO postgres;

--
-- Name: trackpoints_test3_10_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.trackpoints_test3_10_gid_seq OWNED BY public.trackpoints_test3_10.gid;


--
-- Name: trackpoints_test3_12; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints_test3_12 (
    lat numeric,
    lon numeric(11,8),
    dbs numeric(8,2),
    valid boolean,
    gid integer NOT NULL,
    datasetid integer,
    the_geom public.geometry,
    recordingdate timestamp with time zone,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.st_ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((public.geometrytype(the_geom) = 'POINT'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.st_srid(the_geom) = 4326))
);


ALTER TABLE public.trackpoints_test3_12 OWNER TO postgres;

--
-- Name: trackpoints_test3_12_gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.trackpoints_test3_12_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.trackpoints_test3_12_gid_seq OWNER TO postgres;

--
-- Name: trackpoints_test3_12_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.trackpoints_test3_12_gid_seq OWNED BY public.trackpoints_test3_12.gid;


--
-- Name: trackpoints_test3_16; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints_test3_16 (
    lat numeric,
    lon numeric(11,8),
    dbs numeric(8,2),
    valid boolean,
    gid integer NOT NULL,
    datasetid integer,
    the_geom public.geometry,
    recordingdate timestamp with time zone,
    latvar double precision DEFAULT 0,
    lonvar double precision DEFAULT 0,
    depthvar double precision DEFAULT 0,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.st_ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((public.geometrytype(the_geom) = 'POINT'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.st_srid(the_geom) = 4326))
);


ALTER TABLE public.trackpoints_test3_16 OWNER TO postgres;

--
-- Name: trackpoints_test3_16_gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.trackpoints_test3_16_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.trackpoints_test3_16_gid_seq OWNER TO postgres;

--
-- Name: trackpoints_test3_16_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.trackpoints_test3_16_gid_seq OWNED BY public.trackpoints_test3_16.gid;


--
-- Name: trackpoints_test3_8; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trackpoints_test3_8 (
    lat numeric,
    lon numeric(11,8),
    dbs numeric(8,2),
    valid boolean,
    gid integer NOT NULL,
    datasetid integer,
    the_geom public.geometry,
    recordingdate timestamp with time zone,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.st_ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((public.geometrytype(the_geom) = 'POINT'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.st_srid(the_geom) = 4326))
);


ALTER TABLE public.trackpoints_test3_8 OWNER TO postgres;

--
-- Name: trackpoints_test3_8_gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.trackpoints_test3_8_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.trackpoints_test3_8_gid_seq OWNER TO postgres;

--
-- Name: trackpoints_test3_8_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.trackpoints_test3_8_gid_seq OWNED BY public.trackpoints_test3_8.gid;


--
-- Name: tri_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.tri_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.tri_seq OWNER TO postgres;

--
-- Name: triangle_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.triangle_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.triangle_id_seq OWNER TO postgres;

SET default_with_oids = true;

--
-- Name: triangulation; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.triangulation (
    id integer NOT NULL,
    geom public.geometry,
    gid bigint DEFAULT nextval('public.tri_seq'::regclass) NOT NULL,
    CONSTRAINT enforce_dims_geom CHECK ((public.st_ndims(geom) = 3)),
    CONSTRAINT enforce_geotype_geom CHECK (((public.geometrytype(geom) = 'POLYGON'::text) OR (geom IS NULL))),
    CONSTRAINT enforce_srid_geom CHECK ((public.st_srid(geom) = 4326))
);


ALTER TABLE public.triangulation OWNER TO postgres;

--
-- Name: triangulation_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.triangulation_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.triangulation_id_seq OWNER TO postgres;

--
-- Name: triangulation_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.triangulation_id_seq OWNED BY public.triangulation.id;


--
-- Name: user_profiles; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.user_profiles AS
 SELECT user_profiles.user_name,
    user_profiles.password,
    user_profiles.salt,
    user_profiles.attempts,
    user_profiles.last_attempt,
    user_profiles.forename,
    user_profiles.surname,
    user_profiles.country,
    user_profiles.language,
    user_profiles.organisation,
    user_profiles.phone,
    user_profiles.acceptedemailcontact,
    user_profiles.id
   FROM osmapi_tables.user_profiles;


ALTER TABLE public.user_profiles OWNER TO postgres;

--
-- Name: v_tp_raw_render_1; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.v_tp_raw_render_1 AS
 SELECT trackpoints_raw_render_1.gid,
    trackpoints_raw_render_1.track_id,
    trackpoints_raw_render_1.dbs,
    trackpoints_raw_render_1.the_geom
   FROM public.trackpoints_raw_render_1
UNION ALL
 SELECT tp1.gid,
    utr.containertrack AS track_id,
    tp1.dbs,
    tp1.the_geom
   FROM osmapi_tables.user_tracks utr,
    public.trackpoints_raw_render_1 tp1
  WHERE (utr.track_id = tp1.track_id);


ALTER TABLE public.v_tp_raw_render_1 OWNER TO postgres;

--
-- Name: v_tp_raw_render_10; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.v_tp_raw_render_10 AS
 SELECT trackpoints_raw_render_10.gid,
    trackpoints_raw_render_10.track_id,
    trackpoints_raw_render_10.dbs,
    trackpoints_raw_render_10.the_geom
   FROM public.trackpoints_raw_render_10
UNION ALL
 SELECT tp10.gid,
    utr.containertrack AS track_id,
    tp10.dbs,
    tp10.the_geom
   FROM osmapi_tables.user_tracks utr,
    public.trackpoints_raw_render_10 tp10
  WHERE (utr.track_id = tp10.track_id);


ALTER TABLE public.v_tp_raw_render_10 OWNER TO postgres;

--
-- Name: v_tp_raw_render_11; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.v_tp_raw_render_11 AS
 SELECT trackpoints_raw_render_11.gid,
    trackpoints_raw_render_11.track_id,
    trackpoints_raw_render_11.dbs,
    trackpoints_raw_render_11.the_geom
   FROM public.trackpoints_raw_render_11
UNION ALL
 SELECT tp11.gid,
    utr.containertrack AS track_id,
    tp11.dbs,
    tp11.the_geom
   FROM osmapi_tables.user_tracks utr,
    public.trackpoints_raw_render_11 tp11
  WHERE (utr.track_id = tp11.track_id);


ALTER TABLE public.v_tp_raw_render_11 OWNER TO postgres;

--
-- Name: v_tp_raw_render_12; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.v_tp_raw_render_12 AS
 SELECT trackpoints_raw_render_12.gid,
    trackpoints_raw_render_12.track_id,
    trackpoints_raw_render_12.dbs,
    trackpoints_raw_render_12.the_geom
   FROM public.trackpoints_raw_render_12
UNION ALL
 SELECT tp12.gid,
    utr.containertrack AS track_id,
    tp12.dbs,
    tp12.the_geom
   FROM osmapi_tables.user_tracks utr,
    public.trackpoints_raw_render_12 tp12
  WHERE (utr.track_id = tp12.track_id);


ALTER TABLE public.v_tp_raw_render_12 OWNER TO postgres;

--
-- Name: v_tp_raw_render_13; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.v_tp_raw_render_13 AS
 SELECT trackpoints_raw_render_13.gid,
    trackpoints_raw_render_13.track_id,
    trackpoints_raw_render_13.dbs,
    trackpoints_raw_render_13.the_geom
   FROM public.trackpoints_raw_render_13
UNION ALL
 SELECT tp13.gid,
    utr.containertrack AS track_id,
    tp13.dbs,
    tp13.the_geom
   FROM osmapi_tables.user_tracks utr,
    public.trackpoints_raw_render_13 tp13
  WHERE (utr.track_id = tp13.track_id);


ALTER TABLE public.v_tp_raw_render_13 OWNER TO postgres;

--
-- Name: v_tp_raw_render_14; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.v_tp_raw_render_14 AS
 SELECT trackpoints_raw_render_14.gid,
    trackpoints_raw_render_14.track_id,
    trackpoints_raw_render_14.dbs,
    trackpoints_raw_render_14.the_geom
   FROM public.trackpoints_raw_render_14
UNION ALL
 SELECT tp14.gid,
    utr.containertrack AS track_id,
    tp14.dbs,
    tp14.the_geom
   FROM osmapi_tables.user_tracks utr,
    public.trackpoints_raw_render_14 tp14
  WHERE (utr.track_id = tp14.track_id);


ALTER TABLE public.v_tp_raw_render_14 OWNER TO postgres;

--
-- Name: v_tp_raw_render_15; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.v_tp_raw_render_15 AS
 SELECT trackpoints_raw_render_15.gid,
    trackpoints_raw_render_15.track_id,
    trackpoints_raw_render_15.dbs,
    trackpoints_raw_render_15.the_geom
   FROM public.trackpoints_raw_render_15
UNION ALL
 SELECT tp15.gid,
    utr.containertrack AS track_id,
    tp15.dbs,
    tp15.the_geom
   FROM osmapi_tables.user_tracks utr,
    public.trackpoints_raw_render_15 tp15
  WHERE (utr.track_id = tp15.track_id);


ALTER TABLE public.v_tp_raw_render_15 OWNER TO postgres;

--
-- Name: v_tp_raw_render_16; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.v_tp_raw_render_16 AS
 SELECT trackpoints_raw_render_16.gid,
    trackpoints_raw_render_16.track_id,
    trackpoints_raw_render_16.dbs,
    trackpoints_raw_render_16.the_geom
   FROM public.trackpoints_raw_render_16
UNION ALL
 SELECT tp16.gid,
    utr.containertrack AS track_id,
    tp16.dbs,
    tp16.the_geom
   FROM osmapi_tables.user_tracks utr,
    public.trackpoints_raw_render_16 tp16
  WHERE (utr.track_id = tp16.track_id);


ALTER TABLE public.v_tp_raw_render_16 OWNER TO postgres;

--
-- Name: v_tp_raw_render_17; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.v_tp_raw_render_17 AS
 SELECT trackpoints_raw_render_17.gid,
    trackpoints_raw_render_17.track_id,
    trackpoints_raw_render_17.dbs,
    trackpoints_raw_render_17.the_geom
   FROM public.trackpoints_raw_render_17
UNION ALL
 SELECT tp17.gid,
    utr.containertrack AS track_id,
    tp17.dbs,
    tp17.the_geom
   FROM osmapi_tables.user_tracks utr,
    public.trackpoints_raw_render_17 tp17
  WHERE (utr.track_id = tp17.track_id);


ALTER TABLE public.v_tp_raw_render_17 OWNER TO postgres;

--
-- Name: v_tp_raw_render_18; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.v_tp_raw_render_18 AS
 SELECT trackpoints_raw_render_18.gid,
    trackpoints_raw_render_18.track_id,
    trackpoints_raw_render_18.dbs,
    trackpoints_raw_render_18.the_geom
   FROM public.trackpoints_raw_render_18
UNION ALL
 SELECT tp18.gid,
    utr.containertrack AS track_id,
    tp18.dbs,
    tp18.the_geom
   FROM osmapi_tables.user_tracks utr,
    public.trackpoints_raw_render_18 tp18
  WHERE (utr.track_id = tp18.track_id);


ALTER TABLE public.v_tp_raw_render_18 OWNER TO postgres;

--
-- Name: v_tp_raw_render_19; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.v_tp_raw_render_19 AS
 SELECT trackpoints_raw_render_19.gid,
    trackpoints_raw_render_19.track_id,
    trackpoints_raw_render_19.dbs,
    trackpoints_raw_render_19.the_geom
   FROM public.trackpoints_raw_render_19
UNION ALL
 SELECT tp19.gid,
    utr.containertrack AS track_id,
    tp19.dbs,
    tp19.the_geom
   FROM osmapi_tables.user_tracks utr,
    public.trackpoints_raw_render_19 tp19
  WHERE (utr.track_id = tp19.track_id);


ALTER TABLE public.v_tp_raw_render_19 OWNER TO postgres;

--
-- Name: v_tp_raw_render_2; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.v_tp_raw_render_2 AS
 SELECT trackpoints_raw_render_2.gid,
    trackpoints_raw_render_2.track_id,
    trackpoints_raw_render_2.dbs,
    trackpoints_raw_render_2.the_geom
   FROM public.trackpoints_raw_render_2
UNION ALL
 SELECT tp2.gid,
    utr.containertrack AS track_id,
    tp2.dbs,
    tp2.the_geom
   FROM osmapi_tables.user_tracks utr,
    public.trackpoints_raw_render_2 tp2
  WHERE (utr.track_id = tp2.track_id);


ALTER TABLE public.v_tp_raw_render_2 OWNER TO postgres;

--
-- Name: v_tp_raw_render_20; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.v_tp_raw_render_20 AS
 SELECT trackpoints_raw_render_20.gid,
    trackpoints_raw_render_20.track_id,
    trackpoints_raw_render_20.dbs,
    trackpoints_raw_render_20.the_geom
   FROM public.trackpoints_raw_render_20
UNION ALL
 SELECT tp20.gid,
    utr.containertrack AS track_id,
    tp20.dbs,
    tp20.the_geom
   FROM osmapi_tables.user_tracks utr,
    public.trackpoints_raw_render_20 tp20
  WHERE (utr.track_id = tp20.track_id);


ALTER TABLE public.v_tp_raw_render_20 OWNER TO postgres;

--
-- Name: v_tp_raw_render_21; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.v_tp_raw_render_21 AS
 SELECT trackpoints_raw_render_21.gid,
    trackpoints_raw_render_21.track_id,
    trackpoints_raw_render_21.dbs,
    trackpoints_raw_render_21.the_geom
   FROM public.trackpoints_raw_render_21
UNION ALL
 SELECT tp21.gid,
    utr.containertrack AS track_id,
    tp21.dbs,
    tp21.the_geom
   FROM osmapi_tables.user_tracks utr,
    public.trackpoints_raw_render_21 tp21
  WHERE (utr.track_id = tp21.track_id);


ALTER TABLE public.v_tp_raw_render_21 OWNER TO postgres;

--
-- Name: v_tp_raw_render_22; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.v_tp_raw_render_22 AS
 SELECT trackpoints_raw_render_22.gid,
    trackpoints_raw_render_22.track_id,
    trackpoints_raw_render_22.dbs,
    trackpoints_raw_render_22.the_geom
   FROM public.trackpoints_raw_render_22
UNION ALL
 SELECT tp22.gid,
    utr.containertrack AS track_id,
    tp22.dbs,
    tp22.the_geom
   FROM osmapi_tables.user_tracks utr,
    public.trackpoints_raw_render_22 tp22
  WHERE (utr.track_id = tp22.track_id);


ALTER TABLE public.v_tp_raw_render_22 OWNER TO postgres;

--
-- Name: v_tp_raw_render_3; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.v_tp_raw_render_3 AS
 SELECT trackpoints_raw_render_3.gid,
    trackpoints_raw_render_3.track_id,
    trackpoints_raw_render_3.dbs,
    trackpoints_raw_render_3.the_geom
   FROM public.trackpoints_raw_render_3
UNION ALL
 SELECT tp3.gid,
    utr.containertrack AS track_id,
    tp3.dbs,
    tp3.the_geom
   FROM osmapi_tables.user_tracks utr,
    public.trackpoints_raw_render_3 tp3
  WHERE (utr.track_id = tp3.track_id);


ALTER TABLE public.v_tp_raw_render_3 OWNER TO postgres;

--
-- Name: v_tp_raw_render_4; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.v_tp_raw_render_4 AS
 SELECT trackpoints_raw_render_4.gid,
    trackpoints_raw_render_4.track_id,
    trackpoints_raw_render_4.dbs,
    trackpoints_raw_render_4.the_geom
   FROM public.trackpoints_raw_render_4
UNION ALL
 SELECT tp4.gid,
    utr.containertrack AS track_id,
    tp4.dbs,
    tp4.the_geom
   FROM osmapi_tables.user_tracks utr,
    public.trackpoints_raw_render_4 tp4
  WHERE (utr.track_id = tp4.track_id);


ALTER TABLE public.v_tp_raw_render_4 OWNER TO postgres;

--
-- Name: v_tp_raw_render_5; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.v_tp_raw_render_5 AS
 SELECT trackpoints_raw_render_5.gid,
    trackpoints_raw_render_5.track_id,
    trackpoints_raw_render_5.dbs,
    trackpoints_raw_render_5.the_geom
   FROM public.trackpoints_raw_render_5
UNION ALL
 SELECT tp5.gid,
    utr.containertrack AS track_id,
    tp5.dbs,
    tp5.the_geom
   FROM osmapi_tables.user_tracks utr,
    public.trackpoints_raw_render_5 tp5
  WHERE (utr.track_id = tp5.track_id);


ALTER TABLE public.v_tp_raw_render_5 OWNER TO postgres;

--
-- Name: v_tp_raw_render_6; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.v_tp_raw_render_6 AS
 SELECT trackpoints_raw_render_6.gid,
    trackpoints_raw_render_6.track_id,
    trackpoints_raw_render_6.dbs,
    trackpoints_raw_render_6.the_geom
   FROM public.trackpoints_raw_render_6
UNION ALL
 SELECT tp6.gid,
    utr.containertrack AS track_id,
    tp6.dbs,
    tp6.the_geom
   FROM osmapi_tables.user_tracks utr,
    public.trackpoints_raw_render_6 tp6
  WHERE (utr.track_id = tp6.track_id);


ALTER TABLE public.v_tp_raw_render_6 OWNER TO postgres;

--
-- Name: v_tp_raw_render_7; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.v_tp_raw_render_7 AS
 SELECT trackpoints_raw_render_7.gid,
    trackpoints_raw_render_7.track_id,
    trackpoints_raw_render_7.dbs,
    trackpoints_raw_render_7.the_geom
   FROM public.trackpoints_raw_render_7
UNION ALL
 SELECT tp7.gid,
    utr.containertrack AS track_id,
    tp7.dbs,
    tp7.the_geom
   FROM osmapi_tables.user_tracks utr,
    public.trackpoints_raw_render_7 tp7
  WHERE (utr.track_id = tp7.track_id);


ALTER TABLE public.v_tp_raw_render_7 OWNER TO postgres;

--
-- Name: v_tp_raw_render_8; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.v_tp_raw_render_8 AS
 SELECT trackpoints_raw_render_8.gid,
    trackpoints_raw_render_8.track_id,
    trackpoints_raw_render_8.dbs,
    trackpoints_raw_render_8.the_geom
   FROM public.trackpoints_raw_render_8
UNION ALL
 SELECT tp8.gid,
    utr.containertrack AS track_id,
    tp8.dbs,
    tp8.the_geom
   FROM osmapi_tables.user_tracks utr,
    public.trackpoints_raw_render_8 tp8
  WHERE (utr.track_id = tp8.track_id);


ALTER TABLE public.v_tp_raw_render_8 OWNER TO postgres;

--
-- Name: v_tp_raw_render_9; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.v_tp_raw_render_9 AS
 SELECT trackpoints_raw_render_9.gid,
    trackpoints_raw_render_9.track_id,
    trackpoints_raw_render_9.dbs,
    trackpoints_raw_render_9.the_geom
   FROM public.trackpoints_raw_render_9
UNION ALL
 SELECT tp9.gid,
    utr.containertrack AS track_id,
    tp9.dbs,
    tp9.the_geom
   FROM osmapi_tables.user_tracks utr,
    public.trackpoints_raw_render_9 tp9
  WHERE (utr.track_id = tp9.track_id);


ALTER TABLE public.v_tp_raw_render_9 OWNER TO postgres;

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

SET default_with_oids = false;

--
-- Name: water; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.water (
    gid integer NOT NULL,
    fid double precision,
    the_geom public.geometry,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.st_ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((public.geometrytype(the_geom) = 'MULTIPOLYGON'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.st_srid(the_geom) = 4326))
);


ALTER TABLE public.water OWNER TO postgres;

--
-- Name: water_gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.water_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.water_gid_seq OWNER TO postgres;

--
-- Name: water_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.water_gid_seq OWNED BY public.water.gid;


--
-- Name: waterpolygon; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.waterpolygon (
    gid integer NOT NULL,
    fid double precision,
    the_geom public.geometry,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.st_ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((public.geometrytype(the_geom) = 'MULTIPOLYGON'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.st_srid(the_geom) = 0))
);


ALTER TABLE public.waterpolygon OWNER TO postgres;

--
-- Name: waterpolygon_gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.waterpolygon_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.waterpolygon_gid_seq OWNER TO postgres;

--
-- Name: waterpolygon_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.waterpolygon_gid_seq OWNED BY public.waterpolygon.gid;


--
-- Name: zoom_10; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.zoom_10 (
    gid integer NOT NULL,
    id integer,
    orig_fid integer,
    the_geom public.geometry,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.st_ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((public.geometrytype(the_geom) = 'POINT'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.st_srid(the_geom) = 4326))
);


ALTER TABLE public.zoom_10 OWNER TO postgres;

--
-- Name: zoom_10_cor_1_contours; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.zoom_10_cor_1_contours (
    gid integer NOT NULL,
    the_geom public.geometry
);


ALTER TABLE public.zoom_10_cor_1_contours OWNER TO postgres;

--
-- Name: zoom_10_cor_1_points; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.zoom_10_cor_1_points (
    gid integer NOT NULL,
    the_geom public.geometry
);


ALTER TABLE public.zoom_10_cor_1_points OWNER TO postgres;

--
-- Name: zoom_10_fishnet; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.zoom_10_fishnet (
    gid integer NOT NULL,
    id integer,
    the_geom public.geometry,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.st_ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((public.geometrytype(the_geom) = 'MULTIPOLYGON'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.st_srid(the_geom) = 4326))
);


ALTER TABLE public.zoom_10_fishnet OWNER TO postgres;

--
-- Name: zoom_10_fishnet_gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.zoom_10_fishnet_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.zoom_10_fishnet_gid_seq OWNER TO postgres;

--
-- Name: zoom_10_fishnet_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.zoom_10_fishnet_gid_seq OWNED BY public.zoom_10_fishnet.gid;


--
-- Name: zoom_10_gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.zoom_10_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.zoom_10_gid_seq OWNER TO postgres;

--
-- Name: zoom_10_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.zoom_10_gid_seq OWNED BY public.zoom_10.gid;


--
-- Name: zoom_11; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.zoom_11 (
    gid integer NOT NULL,
    oid_ integer,
    shape_leng numeric,
    shape_area numeric,
    orig_fid integer,
    the_geom public.geometry,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.st_ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((public.geometrytype(the_geom) = 'POINT'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.st_srid(the_geom) = 4326))
);


ALTER TABLE public.zoom_11 OWNER TO postgres;

--
-- Name: zoom_11_gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.zoom_11_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.zoom_11_gid_seq OWNER TO postgres;

--
-- Name: zoom_11_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.zoom_11_gid_seq OWNED BY public.zoom_11.gid;


--
-- Name: zoom_12; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.zoom_12 (
    gid integer NOT NULL,
    oid_ integer,
    shape_leng numeric,
    shape_area numeric,
    orig_fid integer,
    the_geom public.geometry,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.st_ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((public.geometrytype(the_geom) = 'POINT'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.st_srid(the_geom) = 4326))
);


ALTER TABLE public.zoom_12 OWNER TO postgres;

--
-- Name: zoom_12_gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.zoom_12_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.zoom_12_gid_seq OWNER TO postgres;

--
-- Name: zoom_12_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.zoom_12_gid_seq OWNED BY public.zoom_12.gid;


--
-- Name: zoom_2; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.zoom_2 (
    gid integer NOT NULL,
    id integer,
    orig_fid integer,
    the_geom public.geometry,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.st_ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((public.geometrytype(the_geom) = 'POINT'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.st_srid(the_geom) = 4326))
);


ALTER TABLE public.zoom_2 OWNER TO postgres;

--
-- Name: zoom_2_cor_1_contours; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.zoom_2_cor_1_contours (
    gid integer NOT NULL,
    the_geom public.geometry
);


ALTER TABLE public.zoom_2_cor_1_contours OWNER TO postgres;

--
-- Name: zoom_2_cor_1_points; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.zoom_2_cor_1_points (
    gid integer NOT NULL,
    the_geom public.geometry
);


ALTER TABLE public.zoom_2_cor_1_points OWNER TO postgres;

--
-- Name: zoom_2_fishnet; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.zoom_2_fishnet (
    gid integer NOT NULL,
    id integer,
    the_geom public.geometry,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.st_ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((public.geometrytype(the_geom) = 'MULTIPOLYGON'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.st_srid(the_geom) = 4326))
);


ALTER TABLE public.zoom_2_fishnet OWNER TO postgres;

--
-- Name: zoom_2_fishnet_gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.zoom_2_fishnet_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.zoom_2_fishnet_gid_seq OWNER TO postgres;

--
-- Name: zoom_2_fishnet_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.zoom_2_fishnet_gid_seq OWNED BY public.zoom_2_fishnet.gid;


--
-- Name: zoom_2_gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.zoom_2_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.zoom_2_gid_seq OWNER TO postgres;

--
-- Name: zoom_2_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.zoom_2_gid_seq OWNED BY public.zoom_2.gid;


--
-- Name: zoom_3; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.zoom_3 (
    gid integer NOT NULL,
    id integer,
    orig_fid integer,
    the_geom public.geometry,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.st_ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((public.geometrytype(the_geom) = 'POINT'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.st_srid(the_geom) = 4326))
);


ALTER TABLE public.zoom_3 OWNER TO postgres;

--
-- Name: zoom_3_cor_1_contours; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.zoom_3_cor_1_contours (
    gid integer NOT NULL,
    the_geom public.geometry
);


ALTER TABLE public.zoom_3_cor_1_contours OWNER TO postgres;

--
-- Name: zoom_3_cor_1_points; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.zoom_3_cor_1_points (
    gid integer NOT NULL,
    the_geom public.geometry
);


ALTER TABLE public.zoom_3_cor_1_points OWNER TO postgres;

--
-- Name: zoom_3_fishnet; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.zoom_3_fishnet (
    gid integer NOT NULL,
    id integer,
    the_geom public.geometry,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.st_ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((public.geometrytype(the_geom) = 'MULTIPOLYGON'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.st_srid(the_geom) = 4326))
);


ALTER TABLE public.zoom_3_fishnet OWNER TO postgres;

--
-- Name: zoom_3_fishnet_gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.zoom_3_fishnet_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.zoom_3_fishnet_gid_seq OWNER TO postgres;

--
-- Name: zoom_3_fishnet_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.zoom_3_fishnet_gid_seq OWNED BY public.zoom_3_fishnet.gid;


--
-- Name: zoom_3_gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.zoom_3_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.zoom_3_gid_seq OWNER TO postgres;

--
-- Name: zoom_3_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.zoom_3_gid_seq OWNED BY public.zoom_3.gid;


--
-- Name: zoom_4; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.zoom_4 (
    gid integer NOT NULL,
    id integer,
    orig_fid integer,
    the_geom public.geometry,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.st_ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((public.geometrytype(the_geom) = 'POINT'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.st_srid(the_geom) = 4326))
);


ALTER TABLE public.zoom_4 OWNER TO postgres;

--
-- Name: zoom_4_cor_1_contours; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.zoom_4_cor_1_contours (
    gid integer NOT NULL,
    the_geom public.geometry
);


ALTER TABLE public.zoom_4_cor_1_contours OWNER TO postgres;

--
-- Name: zoom_4_cor_1_points; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.zoom_4_cor_1_points (
    gid integer NOT NULL,
    the_geom public.geometry
);


ALTER TABLE public.zoom_4_cor_1_points OWNER TO postgres;

--
-- Name: zoom_4_fishnet; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.zoom_4_fishnet (
    gid integer NOT NULL,
    id integer,
    the_geom public.geometry,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.st_ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((public.geometrytype(the_geom) = 'MULTIPOLYGON'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.st_srid(the_geom) = 4326))
);


ALTER TABLE public.zoom_4_fishnet OWNER TO postgres;

--
-- Name: zoom_4_fishnet_gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.zoom_4_fishnet_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.zoom_4_fishnet_gid_seq OWNER TO postgres;

--
-- Name: zoom_4_fishnet_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.zoom_4_fishnet_gid_seq OWNED BY public.zoom_4_fishnet.gid;


--
-- Name: zoom_4_gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.zoom_4_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.zoom_4_gid_seq OWNER TO postgres;

--
-- Name: zoom_4_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.zoom_4_gid_seq OWNED BY public.zoom_4.gid;


--
-- Name: zoom_5_cor_1_contours; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.zoom_5_cor_1_contours (
    gid integer NOT NULL,
    the_geom public.geometry
);


ALTER TABLE public.zoom_5_cor_1_contours OWNER TO postgres;

--
-- Name: zoom_5_cor_1_points; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.zoom_5_cor_1_points (
    gid integer NOT NULL,
    the_geom public.geometry
);


ALTER TABLE public.zoom_5_cor_1_points OWNER TO postgres;

--
-- Name: zoom_5_fishnet; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.zoom_5_fishnet (
    gid integer NOT NULL,
    id integer,
    the_geom public.geometry,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.st_ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((public.geometrytype(the_geom) = 'MULTIPOLYGON'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.st_srid(the_geom) = 4326))
);


ALTER TABLE public.zoom_5_fishnet OWNER TO postgres;

--
-- Name: zoom_5_fishnet_gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.zoom_5_fishnet_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.zoom_5_fishnet_gid_seq OWNER TO postgres;

--
-- Name: zoom_5_fishnet_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.zoom_5_fishnet_gid_seq OWNED BY public.zoom_5_fishnet.gid;


--
-- Name: zoom_6; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.zoom_6 (
    gid integer NOT NULL,
    id integer,
    orig_fid integer,
    the_geom public.geometry,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.st_ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((public.geometrytype(the_geom) = 'POINT'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.st_srid(the_geom) = 4326))
);


ALTER TABLE public.zoom_6 OWNER TO postgres;

--
-- Name: zoom_6_cor_1_contours; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.zoom_6_cor_1_contours (
    gid integer NOT NULL,
    the_geom public.geometry
);


ALTER TABLE public.zoom_6_cor_1_contours OWNER TO postgres;

--
-- Name: zoom_6_cor_1_points; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.zoom_6_cor_1_points (
    gid integer NOT NULL,
    the_geom public.geometry
);


ALTER TABLE public.zoom_6_cor_1_points OWNER TO postgres;

--
-- Name: zoom_6_fishnet; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.zoom_6_fishnet (
    gid integer NOT NULL,
    id integer,
    the_geom public.geometry,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.st_ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((public.geometrytype(the_geom) = 'MULTIPOLYGON'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.st_srid(the_geom) = 4326))
);


ALTER TABLE public.zoom_6_fishnet OWNER TO postgres;

--
-- Name: zoom_6_fishnet_gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.zoom_6_fishnet_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.zoom_6_fishnet_gid_seq OWNER TO postgres;

--
-- Name: zoom_6_fishnet_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.zoom_6_fishnet_gid_seq OWNED BY public.zoom_6_fishnet.gid;


--
-- Name: zoom_6_gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.zoom_6_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.zoom_6_gid_seq OWNER TO postgres;

--
-- Name: zoom_6_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.zoom_6_gid_seq OWNED BY public.zoom_6.gid;


--
-- Name: zoom_7; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.zoom_7 (
    gid integer NOT NULL,
    id integer,
    orig_fid integer,
    the_geom public.geometry,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.st_ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((public.geometrytype(the_geom) = 'POINT'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.st_srid(the_geom) = 4326))
);


ALTER TABLE public.zoom_7 OWNER TO postgres;

--
-- Name: zoom_7_cor_1_contours; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.zoom_7_cor_1_contours (
    gid integer NOT NULL,
    the_geom public.geometry
);


ALTER TABLE public.zoom_7_cor_1_contours OWNER TO postgres;

--
-- Name: zoom_7_cor_1_points; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.zoom_7_cor_1_points (
    gid integer NOT NULL,
    the_geom public.geometry
);


ALTER TABLE public.zoom_7_cor_1_points OWNER TO postgres;

--
-- Name: zoom_7_fishnet; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.zoom_7_fishnet (
    gid integer NOT NULL,
    id integer,
    the_geom public.geometry,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.st_ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((public.geometrytype(the_geom) = 'MULTIPOLYGON'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.st_srid(the_geom) = 4326))
);


ALTER TABLE public.zoom_7_fishnet OWNER TO postgres;

--
-- Name: zoom_7_fishnet ; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."zoom_7_fishnet " (
    gid integer NOT NULL,
    id integer,
    the_geom public.geometry,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.st_ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((public.geometrytype(the_geom) = 'MULTIPOLYGON'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.st_srid(the_geom) = 4326))
);


ALTER TABLE public."zoom_7_fishnet " OWNER TO postgres;

--
-- Name: zoom_7_fishnet _gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public."zoom_7_fishnet _gid_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public."zoom_7_fishnet _gid_seq" OWNER TO postgres;

--
-- Name: zoom_7_fishnet _gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public."zoom_7_fishnet _gid_seq" OWNED BY public."zoom_7_fishnet ".gid;


--
-- Name: zoom_7_fishnet_gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.zoom_7_fishnet_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.zoom_7_fishnet_gid_seq OWNER TO postgres;

--
-- Name: zoom_7_fishnet_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.zoom_7_fishnet_gid_seq OWNED BY public.zoom_7_fishnet.gid;


--
-- Name: zoom_7_gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.zoom_7_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.zoom_7_gid_seq OWNER TO postgres;

--
-- Name: zoom_7_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.zoom_7_gid_seq OWNED BY public.zoom_7.gid;


--
-- Name: zoom_8; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.zoom_8 (
    gid integer NOT NULL,
    id integer,
    orig_fid integer,
    the_geom public.geometry,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.st_ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((public.geometrytype(the_geom) = 'POINT'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.st_srid(the_geom) = 4326))
);


ALTER TABLE public.zoom_8 OWNER TO postgres;

--
-- Name: zoom_8_cor_1_contours; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.zoom_8_cor_1_contours (
    gid integer NOT NULL,
    the_geom public.geometry
);


ALTER TABLE public.zoom_8_cor_1_contours OWNER TO postgres;

--
-- Name: zoom_8_cor_1_points; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.zoom_8_cor_1_points (
    gid integer NOT NULL,
    the_geom public.geometry
);


ALTER TABLE public.zoom_8_cor_1_points OWNER TO postgres;

--
-- Name: zoom_8_fishnet; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.zoom_8_fishnet (
    gid integer NOT NULL,
    id integer,
    the_geom public.geometry,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.st_ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((public.geometrytype(the_geom) = 'MULTIPOLYGON'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.st_srid(the_geom) = 4326))
);


ALTER TABLE public.zoom_8_fishnet OWNER TO postgres;

--
-- Name: zoom_8_fishnet ; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."zoom_8_fishnet " (
    gid integer NOT NULL,
    id integer,
    the_geom public.geometry,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.st_ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((public.geometrytype(the_geom) = 'MULTIPOLYGON'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.st_srid(the_geom) = 4326))
);


ALTER TABLE public."zoom_8_fishnet " OWNER TO postgres;

--
-- Name: zoom_8_fishnet _gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public."zoom_8_fishnet _gid_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public."zoom_8_fishnet _gid_seq" OWNER TO postgres;

--
-- Name: zoom_8_fishnet _gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public."zoom_8_fishnet _gid_seq" OWNED BY public."zoom_8_fishnet ".gid;


--
-- Name: zoom_8_fishnet_gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.zoom_8_fishnet_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.zoom_8_fishnet_gid_seq OWNER TO postgres;

--
-- Name: zoom_8_fishnet_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.zoom_8_fishnet_gid_seq OWNED BY public.zoom_8_fishnet.gid;


--
-- Name: zoom_8_gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.zoom_8_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.zoom_8_gid_seq OWNER TO postgres;

--
-- Name: zoom_8_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.zoom_8_gid_seq OWNED BY public.zoom_8.gid;


--
-- Name: zoom_9; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.zoom_9 (
    gid integer NOT NULL,
    id integer,
    orig_fid integer,
    the_geom public.geometry,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.st_ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((public.geometrytype(the_geom) = 'POINT'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.st_srid(the_geom) = 4326))
);


ALTER TABLE public.zoom_9 OWNER TO postgres;

--
-- Name: zoom_9_cor_1_contours; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.zoom_9_cor_1_contours (
    gid integer NOT NULL,
    the_geom public.geometry
);


ALTER TABLE public.zoom_9_cor_1_contours OWNER TO postgres;

--
-- Name: zoom_9_cor_1_points; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.zoom_9_cor_1_points (
    gid integer NOT NULL,
    the_geom public.geometry
);


ALTER TABLE public.zoom_9_cor_1_points OWNER TO postgres;

--
-- Name: zoom_9_fishnet; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.zoom_9_fishnet (
    gid integer NOT NULL,
    id integer,
    the_geom public.geometry,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.st_ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((public.geometrytype(the_geom) = 'MULTIPOLYGON'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.st_srid(the_geom) = 4326))
);


ALTER TABLE public.zoom_9_fishnet OWNER TO postgres;

--
-- Name: zoom_9_fishnet_gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.zoom_9_fishnet_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.zoom_9_fishnet_gid_seq OWNER TO postgres;

--
-- Name: zoom_9_fishnet_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.zoom_9_fishnet_gid_seq OWNED BY public.zoom_9_fishnet.gid;


--
-- Name: zoom_9_gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.zoom_9_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.zoom_9_gid_seq OWNER TO postgres;

--
-- Name: zoom_9_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.zoom_9_gid_seq OWNED BY public.zoom_9.gid;


--
-- Name: license id; Type: DEFAULT; Schema: osmapi_tables; Owner: postgres
--

ALTER TABLE ONLY osmapi_tables.license ALTER COLUMN id SET DEFAULT nextval('osmapi_tables.license_id_seq'::regclass);


--
-- Name: trackgauges id; Type: DEFAULT; Schema: osmapi_tables; Owner: postgres
--

ALTER TABLE ONLY osmapi_tables.trackgauges ALTER COLUMN id SET DEFAULT nextval('osmapi_tables.trackgauges_id_seq'::regclass);


--
-- Name: vesselconfiguration id; Type: DEFAULT; Schema: osmapi_tables; Owner: postgres
--

ALTER TABLE ONLY osmapi_tables.vesselconfiguration ALTER COLUMN id SET DEFAULT nextval('osmapi_tables.vesselconfiguration_id_seq'::regclass);


--
-- Name: 1000er gid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."1000er" ALTER COLUMN gid SET DEFAULT nextval('public."1000er_gid_seq"'::regclass);


--
-- Name: 2000er gid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."2000er" ALTER COLUMN gid SET DEFAULT nextval('public."2000er_gid_seq"'::regclass);


--
-- Name: big_polygon gid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.big_polygon ALTER COLUMN gid SET DEFAULT nextval('public.big_polygon_gid_seq'::regclass);


--
-- Name: big_polygon2 gid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.big_polygon2 ALTER COLUMN gid SET DEFAULT nextval('public.big_polygon2_gid_seq'::regclass);


--
-- Name: brom_difference gid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.brom_difference ALTER COLUMN gid SET DEFAULT nextval('public.brom_difference_gid_seq'::regclass);


--
-- Name: bsh_points gid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bsh_points ALTER COLUMN gid SET DEFAULT nextval('public.bsh_points_gid_seq'::regclass);


--
-- Name: contourmanual ogc_fid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.contourmanual ALTER COLUMN ogc_fid SET DEFAULT nextval('public.contourmanual_ogc_fid_seq'::regclass);


--
-- Name: contours_cor1 gid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.contours_cor1 ALTER COLUMN gid SET DEFAULT nextval('public.contours_cor1_gid_seq'::regclass);


--
-- Name: contours_cor_1 gid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.contours_cor_1 ALTER COLUMN gid SET DEFAULT nextval('public.contours_cor_1_gid_seq'::regclass);


--
-- Name: contourspitmulti gid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.contourspitmulti ALTER COLUMN gid SET DEFAULT nextval('public.contoursplit_gid_seq'::regclass);


--
-- Name: contoursplit id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.contoursplit ALTER COLUMN id SET DEFAULT nextval('public.contours_id_seq'::regclass);


--
-- Name: crowded_deeps gid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.crowded_deeps ALTER COLUMN gid SET DEFAULT nextval('public.crowded_deeps_gid_seq'::regclass);


--
-- Name: crowded_osm gid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.crowded_osm ALTER COLUMN gid SET DEFAULT nextval('public.crowded_osm_gid_seq'::regclass);


--
-- Name: crowed_deeps gid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.crowed_deeps ALTER COLUMN gid SET DEFAULT nextval('public.crowed_deeps_gid_seq'::regclass);


--
-- Name: gebco_contours_2014 gid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.gebco_contours_2014 ALTER COLUMN gid SET DEFAULT nextval('public.gebco_contours_2014_gid_seq'::regclass);


--
-- Name: gebco_poly_100 gid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.gebco_poly_100 ALTER COLUMN gid SET DEFAULT nextval('public.gebco_poly_100_gid_seq'::regclass);


--
-- Name: gebco_poly_2014 gid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.gebco_poly_2014 ALTER COLUMN gid SET DEFAULT nextval('public.gebco_poly_2014_gid_seq'::regclass);


--
-- Name: gueltig gid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.gueltig ALTER COLUMN gid SET DEFAULT nextval('public.gueltig_gid_seq'::regclass);


--
-- Name: lat gid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.lat ALTER COLUMN gid SET DEFAULT nextval('public.lat_gid_seq'::regclass);


--
-- Name: shallowwater id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.shallowwater ALTER COLUMN id SET DEFAULT nextval('public.ocean_id_seq'::regclass);


--
-- Name: split gid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.split ALTER COLUMN gid SET DEFAULT nextval('public.split_gid_seq'::regclass);


--
-- Name: trackpoints gid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints ALTER COLUMN gid SET DEFAULT nextval('public.trackpoints_gid_seq'::regclass);


--
-- Name: trackpoints_cor1 gid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_cor1 ALTER COLUMN gid SET DEFAULT nextval('public.trackpoints_cor1_gid_seq'::regclass);


--
-- Name: trackpoints_cor_1 gid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_cor_1 ALTER COLUMN gid SET DEFAULT nextval('public.trackpoints_cor_1_gid_seq'::regclass);


--
-- Name: trackpoints_cortest gid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_cortest ALTER COLUMN gid SET DEFAULT nextval('public.trackpoints_cortest_gid_seq'::regclass);


--
-- Name: trackpoints_empty gid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_empty ALTER COLUMN gid SET DEFAULT nextval('public.trackpoints_empty_gid_seq'::regclass);


--
-- Name: trackpoints_raw_10 gid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_raw_10 ALTER COLUMN gid SET DEFAULT nextval('public.trackpoints_raw_10_gid_seq'::regclass);


--
-- Name: trackpoints_raw_12 gid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_raw_12 ALTER COLUMN gid SET DEFAULT nextval('public.trackpoints_raw_12_gid_seq'::regclass);


--
-- Name: trackpoints_raw_16 gid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_raw_16 ALTER COLUMN gid SET DEFAULT nextval('public.trackpoints_raw_16_gid_seq'::regclass);


--
-- Name: trackpoints_raw_8 gid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_raw_8 ALTER COLUMN gid SET DEFAULT nextval('public.trackpoints_raw_8_gid_seq'::regclass);


--
-- Name: trackpoints_raw_filter_10 gid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_raw_filter_10 ALTER COLUMN gid SET DEFAULT nextval('public.trackpoints_raw_filter_10_gid_seq'::regclass);


--
-- Name: trackpoints_raw_filter_12 gid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_raw_filter_12 ALTER COLUMN gid SET DEFAULT nextval('public.trackpoints_raw_filter_12_gid_seq'::regclass);


--
-- Name: trackpoints_raw_filter_16 gid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_raw_filter_16 ALTER COLUMN gid SET DEFAULT nextval('public.trackpoints_raw_filter_16_gid_seq'::regclass);


--
-- Name: trackpoints_raw_filter_8 gid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_raw_filter_8 ALTER COLUMN gid SET DEFAULT nextval('public.trackpoints_raw_filter_8_gid_seq'::regclass);


--
-- Name: trackpoints_raw_temp_10 gid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_raw_temp_10 ALTER COLUMN gid SET DEFAULT nextval('public.trackpoints_raw_temp_10_gid_seq'::regclass);


--
-- Name: trackpoints_raw_temp_12 gid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_raw_temp_12 ALTER COLUMN gid SET DEFAULT nextval('public.trackpoints_raw_temp_12_gid_seq'::regclass);


--
-- Name: trackpoints_raw_temp_16 gid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_raw_temp_16 ALTER COLUMN gid SET DEFAULT nextval('public.trackpoints_raw_temp_16_gid_seq'::regclass);


--
-- Name: trackpoints_raw_temp_8 gid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_raw_temp_8 ALTER COLUMN gid SET DEFAULT nextval('public.trackpoints_raw_temp_8_gid_seq'::regclass);


--
-- Name: trackpoints_test1_10 gid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_test1_10 ALTER COLUMN gid SET DEFAULT nextval('public.trackpoints_test1_10_gid_seq'::regclass);


--
-- Name: trackpoints_test1_12 gid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_test1_12 ALTER COLUMN gid SET DEFAULT nextval('public.trackpoints_test1_12_gid_seq'::regclass);


--
-- Name: trackpoints_test1_16 gid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_test1_16 ALTER COLUMN gid SET DEFAULT nextval('public.trackpoints_test1_16_gid_seq'::regclass);


--
-- Name: trackpoints_test1_8 gid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_test1_8 ALTER COLUMN gid SET DEFAULT nextval('public.trackpoints_test1_8_gid_seq'::regclass);


--
-- Name: trackpoints_test2_10 gid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_test2_10 ALTER COLUMN gid SET DEFAULT nextval('public.trackpoints_test2_10_gid_seq'::regclass);


--
-- Name: trackpoints_test2_12 gid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_test2_12 ALTER COLUMN gid SET DEFAULT nextval('public.trackpoints_test2_12_gid_seq'::regclass);


--
-- Name: trackpoints_test2_16 gid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_test2_16 ALTER COLUMN gid SET DEFAULT nextval('public.trackpoints_test2_16_gid_seq'::regclass);


--
-- Name: trackpoints_test2_8 gid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_test2_8 ALTER COLUMN gid SET DEFAULT nextval('public.trackpoints_test2_8_gid_seq'::regclass);


--
-- Name: trackpoints_test3_10 gid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_test3_10 ALTER COLUMN gid SET DEFAULT nextval('public.trackpoints_test3_10_gid_seq'::regclass);


--
-- Name: trackpoints_test3_12 gid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_test3_12 ALTER COLUMN gid SET DEFAULT nextval('public.trackpoints_test3_12_gid_seq'::regclass);


--
-- Name: trackpoints_test3_16 gid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_test3_16 ALTER COLUMN gid SET DEFAULT nextval('public.trackpoints_test3_16_gid_seq'::regclass);


--
-- Name: trackpoints_test3_8 gid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_test3_8 ALTER COLUMN gid SET DEFAULT nextval('public.trackpoints_test3_8_gid_seq'::regclass);


--
-- Name: triangulation id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.triangulation ALTER COLUMN id SET DEFAULT nextval('public.triangulation_id_seq'::regclass);


--
-- Name: water gid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.water ALTER COLUMN gid SET DEFAULT nextval('public.water_gid_seq'::regclass);


--
-- Name: waterpolygon gid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.waterpolygon ALTER COLUMN gid SET DEFAULT nextval('public.waterpolygon_gid_seq'::regclass);


--
-- Name: zoom_10 gid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.zoom_10 ALTER COLUMN gid SET DEFAULT nextval('public.zoom_10_gid_seq'::regclass);


--
-- Name: zoom_10_fishnet gid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.zoom_10_fishnet ALTER COLUMN gid SET DEFAULT nextval('public.zoom_10_fishnet_gid_seq'::regclass);


--
-- Name: zoom_11 gid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.zoom_11 ALTER COLUMN gid SET DEFAULT nextval('public.zoom_11_gid_seq'::regclass);


--
-- Name: zoom_12 gid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.zoom_12 ALTER COLUMN gid SET DEFAULT nextval('public.zoom_12_gid_seq'::regclass);


--
-- Name: zoom_2 gid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.zoom_2 ALTER COLUMN gid SET DEFAULT nextval('public.zoom_2_gid_seq'::regclass);


--
-- Name: zoom_2_fishnet gid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.zoom_2_fishnet ALTER COLUMN gid SET DEFAULT nextval('public.zoom_2_fishnet_gid_seq'::regclass);


--
-- Name: zoom_3 gid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.zoom_3 ALTER COLUMN gid SET DEFAULT nextval('public.zoom_3_gid_seq'::regclass);


--
-- Name: zoom_3_fishnet gid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.zoom_3_fishnet ALTER COLUMN gid SET DEFAULT nextval('public.zoom_3_fishnet_gid_seq'::regclass);


--
-- Name: zoom_4 gid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.zoom_4 ALTER COLUMN gid SET DEFAULT nextval('public.zoom_4_gid_seq'::regclass);


--
-- Name: zoom_4_fishnet gid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.zoom_4_fishnet ALTER COLUMN gid SET DEFAULT nextval('public.zoom_4_fishnet_gid_seq'::regclass);


--
-- Name: zoom_5_fishnet gid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.zoom_5_fishnet ALTER COLUMN gid SET DEFAULT nextval('public.zoom_5_fishnet_gid_seq'::regclass);


--
-- Name: zoom_6 gid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.zoom_6 ALTER COLUMN gid SET DEFAULT nextval('public.zoom_6_gid_seq'::regclass);


--
-- Name: zoom_6_fishnet gid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.zoom_6_fishnet ALTER COLUMN gid SET DEFAULT nextval('public.zoom_6_fishnet_gid_seq'::regclass);


--
-- Name: zoom_7 gid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.zoom_7 ALTER COLUMN gid SET DEFAULT nextval('public.zoom_7_gid_seq'::regclass);


--
-- Name: zoom_7_fishnet gid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.zoom_7_fishnet ALTER COLUMN gid SET DEFAULT nextval('public.zoom_7_fishnet_gid_seq'::regclass);


--
-- Name: zoom_7_fishnet  gid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."zoom_7_fishnet " ALTER COLUMN gid SET DEFAULT nextval('public."zoom_7_fishnet _gid_seq"'::regclass);


--
-- Name: zoom_8 gid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.zoom_8 ALTER COLUMN gid SET DEFAULT nextval('public.zoom_8_gid_seq'::regclass);


--
-- Name: zoom_8_fishnet gid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.zoom_8_fishnet ALTER COLUMN gid SET DEFAULT nextval('public.zoom_8_fishnet_gid_seq'::regclass);


--
-- Name: zoom_8_fishnet  gid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."zoom_8_fishnet " ALTER COLUMN gid SET DEFAULT nextval('public."zoom_8_fishnet _gid_seq"'::regclass);


--
-- Name: zoom_9 gid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.zoom_9 ALTER COLUMN gid SET DEFAULT nextval('public.zoom_9_gid_seq'::regclass);


--
-- Name: zoom_9_fishnet gid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.zoom_9_fishnet ALTER COLUMN gid SET DEFAULT nextval('public.zoom_9_fishnet_gid_seq'::regclass);


--
-- Name: depthsensor dse_pk; Type: CONSTRAINT; Schema: osmapi_tables; Owner: postgres
--

ALTER TABLE ONLY osmapi_tables.depthsensor
    ADD CONSTRAINT dse_pk PRIMARY KEY (id);


--
-- Name: gauge gauge_pkey; Type: CONSTRAINT; Schema: osmapi_tables; Owner: postgres
--

ALTER TABLE ONLY osmapi_tables.gauge
    ADD CONSTRAINT gauge_pkey PRIMARY KEY (id);


--
-- Name: gaugemeasurement gaugemeasurement_unique; Type: CONSTRAINT; Schema: osmapi_tables; Owner: postgres
--

ALTER TABLE ONLY osmapi_tables.gaugemeasurement
    ADD CONSTRAINT gaugemeasurement_unique UNIQUE (gaugeid, "time");


--
-- Name: license license_pkey; Type: CONSTRAINT; Schema: osmapi_tables; Owner: postgres
--

ALTER TABLE ONLY osmapi_tables.license
    ADD CONSTRAINT license_pkey PRIMARY KEY (id);


--
-- Name: rpl_journal_shadow pk_rpl_j; Type: CONSTRAINT; Schema: osmapi_tables; Owner: postgres
--

ALTER TABLE ONLY osmapi_tables.rpl_journal_shadow
    ADD CONSTRAINT pk_rpl_j PRIMARY KEY (id);


--
-- Name: rpl_journal pk_upr_j; Type: CONSTRAINT; Schema: osmapi_tables; Owner: postgres
--

ALTER TABLE ONLY osmapi_tables.rpl_journal
    ADD CONSTRAINT pk_upr_j PRIMARY KEY (id);


--
-- Name: sbassensor sse_pk; Type: CONSTRAINT; Schema: osmapi_tables; Owner: postgres
--

ALTER TABLE ONLY osmapi_tables.sbassensor
    ADD CONSTRAINT sse_pk PRIMARY KEY (id);


--
-- Name: track_info tif_pk; Type: CONSTRAINT; Schema: osmapi_tables; Owner: postgres
--

ALTER TABLE ONLY osmapi_tables.track_info
    ADD CONSTRAINT tif_pk PRIMARY KEY (id);


--
-- Name: trackgauges trackgauges_pkey; Type: CONSTRAINT; Schema: osmapi_tables; Owner: postgres
--

ALTER TABLE ONLY osmapi_tables.trackgauges
    ADD CONSTRAINT trackgauges_pkey PRIMARY KEY (id);


--
-- Name: user_profiles upr_name_uk; Type: CONSTRAINT; Schema: osmapi_tables; Owner: postgres
--

ALTER TABLE ONLY osmapi_tables.user_profiles
    ADD CONSTRAINT upr_name_uk UNIQUE (user_name);


--
-- Name: user_profiles upr_pk; Type: CONSTRAINT; Schema: osmapi_tables; Owner: postgres
--

ALTER TABLE ONLY osmapi_tables.user_profiles
    ADD CONSTRAINT upr_pk PRIMARY KEY (id);


--
-- Name: user_tracks user_tracks_pkey; Type: CONSTRAINT; Schema: osmapi_tables; Owner: postgres
--

ALTER TABLE ONLY osmapi_tables.user_tracks
    ADD CONSTRAINT user_tracks_pkey PRIMARY KEY (track_id);


--
-- Name: vesselconfiguration vesselconfiguration_pkey; Type: CONSTRAINT; Schema: osmapi_tables; Owner: postgres
--

ALTER TABLE ONLY osmapi_tables.vesselconfiguration
    ADD CONSTRAINT vesselconfiguration_pkey PRIMARY KEY (id);


--
-- Name: mergerun mer_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.mergerun
    ADD CONSTRAINT mer_pk PRIMARY KEY (id);


--
-- Name: rpl_journal pk_rpl_j; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.rpl_journal
    ADD CONSTRAINT pk_rpl_j PRIMARY KEY (id);


--
-- Name: track_mergerun tmr_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.track_mergerun
    ADD CONSTRAINT tmr_pk PRIMARY KEY (id);


--
-- Name: trackpoints_raw_filter_10 tpr_10_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_raw_filter_10
    ADD CONSTRAINT tpr_10_pk PRIMARY KEY (gid);


--
-- Name: trackpoints_raw_filter_12 tpr_12_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_raw_filter_12
    ADD CONSTRAINT tpr_12_pk PRIMARY KEY (gid);


--
-- Name: trackpoints_raw_filter_16 tpr_16_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_raw_filter_16
    ADD CONSTRAINT tpr_16_pk PRIMARY KEY (gid);


--
-- Name: trackpoints_raw_filter_8 tpr_8_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_raw_filter_8
    ADD CONSTRAINT tpr_8_pk PRIMARY KEY (gid);


--
-- Name: trackpoints_raw_temp_16 tpr_tmp_16_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_raw_temp_16
    ADD CONSTRAINT tpr_tmp_16_pk PRIMARY KEY (gid);


--
-- Name: trackpoints_raw_merge_10 trackpoints_raw_merge_10_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_raw_merge_10
    ADD CONSTRAINT trackpoints_raw_merge_10_pk PRIMARY KEY (gid);


--
-- Name: trackpoints_raw_merge_11 trackpoints_raw_merge_11_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_raw_merge_11
    ADD CONSTRAINT trackpoints_raw_merge_11_pk PRIMARY KEY (gid);


--
-- Name: trackpoints_raw_merge_12 trackpoints_raw_merge_12_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_raw_merge_12
    ADD CONSTRAINT trackpoints_raw_merge_12_pk PRIMARY KEY (gid);


--
-- Name: trackpoints_raw_merge_13 trackpoints_raw_merge_13_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_raw_merge_13
    ADD CONSTRAINT trackpoints_raw_merge_13_pk PRIMARY KEY (gid);


--
-- Name: trackpoints_raw_merge_14 trackpoints_raw_merge_14_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_raw_merge_14
    ADD CONSTRAINT trackpoints_raw_merge_14_pk PRIMARY KEY (gid);


--
-- Name: trackpoints_raw_merge_15 trackpoints_raw_merge_15_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_raw_merge_15
    ADD CONSTRAINT trackpoints_raw_merge_15_pk PRIMARY KEY (gid);


--
-- Name: trackpoints_raw_merge_16 trackpoints_raw_merge_16_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_raw_merge_16
    ADD CONSTRAINT trackpoints_raw_merge_16_pk PRIMARY KEY (gid);


--
-- Name: trackpoints_raw_merge_17 trackpoints_raw_merge_17_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_raw_merge_17
    ADD CONSTRAINT trackpoints_raw_merge_17_pk PRIMARY KEY (gid);


--
-- Name: trackpoints_raw_merge_18 trackpoints_raw_merge_18_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_raw_merge_18
    ADD CONSTRAINT trackpoints_raw_merge_18_pk PRIMARY KEY (gid);


--
-- Name: trackpoints_raw_merge_19 trackpoints_raw_merge_19_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_raw_merge_19
    ADD CONSTRAINT trackpoints_raw_merge_19_pk PRIMARY KEY (gid);


--
-- Name: trackpoints_raw_merge_1 trackpoints_raw_merge_1_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_raw_merge_1
    ADD CONSTRAINT trackpoints_raw_merge_1_pk PRIMARY KEY (gid);


--
-- Name: trackpoints_raw_merge_20 trackpoints_raw_merge_20_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_raw_merge_20
    ADD CONSTRAINT trackpoints_raw_merge_20_pk PRIMARY KEY (gid);


--
-- Name: trackpoints_raw_merge_21 trackpoints_raw_merge_21_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_raw_merge_21
    ADD CONSTRAINT trackpoints_raw_merge_21_pk PRIMARY KEY (gid);


--
-- Name: trackpoints_raw_merge_22 trackpoints_raw_merge_22_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_raw_merge_22
    ADD CONSTRAINT trackpoints_raw_merge_22_pk PRIMARY KEY (gid);


--
-- Name: trackpoints_raw_merge_2 trackpoints_raw_merge_2_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_raw_merge_2
    ADD CONSTRAINT trackpoints_raw_merge_2_pk PRIMARY KEY (gid);


--
-- Name: trackpoints_raw_merge_3 trackpoints_raw_merge_3_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_raw_merge_3
    ADD CONSTRAINT trackpoints_raw_merge_3_pk PRIMARY KEY (gid);


--
-- Name: trackpoints_raw_merge_4 trackpoints_raw_merge_4_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_raw_merge_4
    ADD CONSTRAINT trackpoints_raw_merge_4_pk PRIMARY KEY (gid);


--
-- Name: trackpoints_raw_merge_5 trackpoints_raw_merge_5_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_raw_merge_5
    ADD CONSTRAINT trackpoints_raw_merge_5_pk PRIMARY KEY (gid);


--
-- Name: trackpoints_raw_merge_6 trackpoints_raw_merge_6_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_raw_merge_6
    ADD CONSTRAINT trackpoints_raw_merge_6_pk PRIMARY KEY (gid);


--
-- Name: trackpoints_raw_merge_7 trackpoints_raw_merge_7_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_raw_merge_7
    ADD CONSTRAINT trackpoints_raw_merge_7_pk PRIMARY KEY (gid);


--
-- Name: trackpoints_raw_merge_8 trackpoints_raw_merge_8_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_raw_merge_8
    ADD CONSTRAINT trackpoints_raw_merge_8_pk PRIMARY KEY (gid);


--
-- Name: trackpoints_raw_merge_9 trackpoints_raw_merge_9_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_raw_merge_9
    ADD CONSTRAINT trackpoints_raw_merge_9_pk PRIMARY KEY (gid);


--
-- Name: trackpoints_raw_render_10 trackpoints_raw_render_10_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_raw_render_10
    ADD CONSTRAINT trackpoints_raw_render_10_pk PRIMARY KEY (gid);


--
-- Name: trackpoints_raw_render_11 trackpoints_raw_render_11_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_raw_render_11
    ADD CONSTRAINT trackpoints_raw_render_11_pk PRIMARY KEY (gid);


--
-- Name: trackpoints_raw_render_12 trackpoints_raw_render_12_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_raw_render_12
    ADD CONSTRAINT trackpoints_raw_render_12_pk PRIMARY KEY (gid);


--
-- Name: trackpoints_raw_render_13 trackpoints_raw_render_13_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_raw_render_13
    ADD CONSTRAINT trackpoints_raw_render_13_pk PRIMARY KEY (gid);


--
-- Name: trackpoints_raw_render_14 trackpoints_raw_render_14_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_raw_render_14
    ADD CONSTRAINT trackpoints_raw_render_14_pk PRIMARY KEY (gid);


--
-- Name: trackpoints_raw_render_15 trackpoints_raw_render_15_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_raw_render_15
    ADD CONSTRAINT trackpoints_raw_render_15_pk PRIMARY KEY (gid);


--
-- Name: trackpoints_raw_render_16 trackpoints_raw_render_16_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_raw_render_16
    ADD CONSTRAINT trackpoints_raw_render_16_pk PRIMARY KEY (gid);


--
-- Name: trackpoints_raw_render_17 trackpoints_raw_render_17_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_raw_render_17
    ADD CONSTRAINT trackpoints_raw_render_17_pk PRIMARY KEY (gid);


--
-- Name: trackpoints_raw_render_18 trackpoints_raw_render_18_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_raw_render_18
    ADD CONSTRAINT trackpoints_raw_render_18_pk PRIMARY KEY (gid);


--
-- Name: trackpoints_raw_render_19 trackpoints_raw_render_19_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_raw_render_19
    ADD CONSTRAINT trackpoints_raw_render_19_pk PRIMARY KEY (gid);


--
-- Name: trackpoints_raw_render_1 trackpoints_raw_render_1_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_raw_render_1
    ADD CONSTRAINT trackpoints_raw_render_1_pk PRIMARY KEY (gid);


--
-- Name: trackpoints_raw_render_20 trackpoints_raw_render_20_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_raw_render_20
    ADD CONSTRAINT trackpoints_raw_render_20_pk PRIMARY KEY (gid);


--
-- Name: trackpoints_raw_render_21 trackpoints_raw_render_21_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_raw_render_21
    ADD CONSTRAINT trackpoints_raw_render_21_pk PRIMARY KEY (gid);


--
-- Name: trackpoints_raw_render_22 trackpoints_raw_render_22_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_raw_render_22
    ADD CONSTRAINT trackpoints_raw_render_22_pk PRIMARY KEY (gid);


--
-- Name: trackpoints_raw_render_2 trackpoints_raw_render_2_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_raw_render_2
    ADD CONSTRAINT trackpoints_raw_render_2_pk PRIMARY KEY (gid);


--
-- Name: trackpoints_raw_render_3 trackpoints_raw_render_3_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_raw_render_3
    ADD CONSTRAINT trackpoints_raw_render_3_pk PRIMARY KEY (gid);


--
-- Name: trackpoints_raw_render_4 trackpoints_raw_render_4_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_raw_render_4
    ADD CONSTRAINT trackpoints_raw_render_4_pk PRIMARY KEY (gid);


--
-- Name: trackpoints_raw_render_5 trackpoints_raw_render_5_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_raw_render_5
    ADD CONSTRAINT trackpoints_raw_render_5_pk PRIMARY KEY (gid);


--
-- Name: trackpoints_raw_render_6 trackpoints_raw_render_6_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_raw_render_6
    ADD CONSTRAINT trackpoints_raw_render_6_pk PRIMARY KEY (gid);


--
-- Name: trackpoints_raw_render_7 trackpoints_raw_render_7_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_raw_render_7
    ADD CONSTRAINT trackpoints_raw_render_7_pk PRIMARY KEY (gid);


--
-- Name: trackpoints_raw_render_8 trackpoints_raw_render_8_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_raw_render_8
    ADD CONSTRAINT trackpoints_raw_render_8_pk PRIMARY KEY (gid);


--
-- Name: trackpoints_raw_render_9 trackpoints_raw_render_9_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trackpoints_raw_render_9
    ADD CONSTRAINT trackpoints_raw_render_9_pk PRIMARY KEY (gid);


--
-- Name: triangulation tri_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.triangulation
    ADD CONSTRAINT tri_pk PRIMARY KEY (gid);


--
-- Name: fki_gaugemeasurement_fkey; Type: INDEX; Schema: osmapi_tables; Owner: postgres
--

CREATE INDEX fki_gaugemeasurement_fkey ON osmapi_tables.gaugemeasurement USING btree (gaugeid);


--
-- Name: rpl_j_s_id_new; Type: INDEX; Schema: osmapi_tables; Owner: postgres
--

CREATE INDEX rpl_j_s_id_new ON osmapi_tables.rpl_journal_shadow USING btree (id) WHERE (copied IS NULL);


--
-- Name: tif_tra_fk_i; Type: INDEX; Schema: osmapi_tables; Owner: postgres
--

CREATE INDEX tif_tra_fk_i ON osmapi_tables.track_info USING btree (tra_id);


--
-- Name: utr_state_sub; Type: INDEX; Schema: osmapi_tables; Owner: postgres
--

CREATE INDEX utr_state_sub ON osmapi_tables.user_tracks USING btree (upload_state) WHERE (containertrack IS NOT NULL);


--
-- Name: utr_state_top; Type: INDEX; Schema: osmapi_tables; Owner: postgres
--

CREATE INDEX utr_state_top ON osmapi_tables.user_tracks USING btree (upload_state) WHERE (is_container AND (num_points > 0));


--
-- Name: utr_upr_fk_i; Type: INDEX; Schema: osmapi_tables; Owner: postgres
--

CREATE INDEX utr_upr_fk_i ON osmapi_tables.user_tracks USING btree (upr_id);


--
-- Name: utr_utr_fk_i; Type: INDEX; Schema: osmapi_tables; Owner: postgres
--

CREATE INDEX utr_utr_fk_i ON osmapi_tables.user_tracks USING btree (containertrack);


--
-- Name: utr_vcf_fk_i; Type: INDEX; Schema: osmapi_tables; Owner: postgres
--

CREATE INDEX utr_vcf_fk_i ON osmapi_tables.user_tracks USING btree (vesselconfigid);


--
-- Name: vcf_upr_fk_i; Type: INDEX; Schema: osmapi_tables; Owner: postgres
--

CREATE INDEX vcf_upr_fk_i ON osmapi_tables.vesselconfiguration USING btree (upr_id);


--
-- Name: contoursplit_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX contoursplit_index ON public.contoursplit USING gist (the_geom);


--
-- Name: tmr_mer_fk_i; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX tmr_mer_fk_i ON public.track_mergerun USING btree (mer_id);


--
-- Name: tmr_tra_fk_i; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX tmr_tra_fk_i ON public.track_mergerun USING btree (tra_id);


--
-- Name: tpr16_tid; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX tpr16_tid ON public.trackpoints_raw_filter_16 USING btree (datasetid);


--
-- Name: tpr16_tmp_tid; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX tpr16_tmp_tid ON public.trackpoints_raw_temp_16 USING btree (datasetid);


--
-- Name: tpr_16_did_gid; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX tpr_16_did_gid ON public.trackpoints_raw_filter_16 USING btree (datasetid, gid);


--
-- Name: tpr_16_tmp_did_gid; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX tpr_16_tmp_did_gid ON public.trackpoints_raw_temp_16 USING btree (datasetid, gid);


--
-- Name: trackpoints_empty_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_empty_index ON public.trackpoints_empty USING gist (the_geom);


--
-- Name: trackpoints_raw_10_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_10_index ON public.trackpoints_raw_10 USING gist (the_geom);


--
-- Name: trackpoints_raw_12_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_12_index ON public.trackpoints_raw_12 USING gist (the_geom);


--
-- Name: trackpoints_raw_16_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_16_index ON public.trackpoints_raw_16 USING gist (the_geom);


--
-- Name: trackpoints_raw_8_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_8_index ON public.trackpoints_raw_8 USING gist (the_geom);


--
-- Name: trackpoints_raw_filter_10_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_filter_10_index ON public.trackpoints_raw_filter_10 USING gist (the_geom);


--
-- Name: trackpoints_raw_filter_12_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_filter_12_index ON public.trackpoints_raw_filter_12 USING gist (the_geom);


--
-- Name: trackpoints_raw_filter_16_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_filter_16_index ON public.trackpoints_raw_filter_16 USING gist (the_geom);


--
-- Name: trackpoints_raw_filter_8_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_filter_8_index ON public.trackpoints_raw_filter_8 USING gist (the_geom);


--
-- Name: trackpoints_raw_merge_10_geom; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_merge_10_geom ON public.trackpoints_raw_merge_10 USING gist (the_geom);


--
-- Name: trackpoints_raw_merge_11_geom; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_merge_11_geom ON public.trackpoints_raw_merge_11 USING gist (the_geom);


--
-- Name: trackpoints_raw_merge_12_geom; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_merge_12_geom ON public.trackpoints_raw_merge_12 USING gist (the_geom);


--
-- Name: trackpoints_raw_merge_13_geom; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_merge_13_geom ON public.trackpoints_raw_merge_13 USING gist (the_geom);


--
-- Name: trackpoints_raw_merge_14_geom; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_merge_14_geom ON public.trackpoints_raw_merge_14 USING gist (the_geom);


--
-- Name: trackpoints_raw_merge_15_geom; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_merge_15_geom ON public.trackpoints_raw_merge_15 USING gist (the_geom);


--
-- Name: trackpoints_raw_merge_16_geom; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_merge_16_geom ON public.trackpoints_raw_merge_16 USING gist (the_geom);


--
-- Name: trackpoints_raw_merge_17_geom; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_merge_17_geom ON public.trackpoints_raw_merge_17 USING gist (the_geom);


--
-- Name: trackpoints_raw_merge_18_geom; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_merge_18_geom ON public.trackpoints_raw_merge_18 USING gist (the_geom);


--
-- Name: trackpoints_raw_merge_19_geom; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_merge_19_geom ON public.trackpoints_raw_merge_19 USING gist (the_geom);


--
-- Name: trackpoints_raw_merge_1_geom; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_merge_1_geom ON public.trackpoints_raw_merge_1 USING gist (the_geom);


--
-- Name: trackpoints_raw_merge_20_geom; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_merge_20_geom ON public.trackpoints_raw_merge_20 USING gist (the_geom);


--
-- Name: trackpoints_raw_merge_21_geom; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_merge_21_geom ON public.trackpoints_raw_merge_21 USING gist (the_geom);


--
-- Name: trackpoints_raw_merge_22_geom; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_merge_22_geom ON public.trackpoints_raw_merge_22 USING gist (the_geom);


--
-- Name: trackpoints_raw_merge_2_geom; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_merge_2_geom ON public.trackpoints_raw_merge_2 USING gist (the_geom);


--
-- Name: trackpoints_raw_merge_3_geom; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_merge_3_geom ON public.trackpoints_raw_merge_3 USING gist (the_geom);


--
-- Name: trackpoints_raw_merge_4_geom; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_merge_4_geom ON public.trackpoints_raw_merge_4 USING gist (the_geom);


--
-- Name: trackpoints_raw_merge_5_geom; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_merge_5_geom ON public.trackpoints_raw_merge_5 USING gist (the_geom);


--
-- Name: trackpoints_raw_merge_6_geom; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_merge_6_geom ON public.trackpoints_raw_merge_6 USING gist (the_geom);


--
-- Name: trackpoints_raw_merge_7_geom; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_merge_7_geom ON public.trackpoints_raw_merge_7 USING gist (the_geom);


--
-- Name: trackpoints_raw_merge_8_geom; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_merge_8_geom ON public.trackpoints_raw_merge_8 USING gist (the_geom);


--
-- Name: trackpoints_raw_merge_9_geom; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_merge_9_geom ON public.trackpoints_raw_merge_9 USING gist (the_geom);


--
-- Name: trackpoints_raw_render_10_geom; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_render_10_geom ON public.trackpoints_raw_render_10 USING gist (the_geom);


--
-- Name: trackpoints_raw_render_10_track; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_render_10_track ON public.trackpoints_raw_render_10 USING btree (track_id);


--
-- Name: trackpoints_raw_render_11_geom; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_render_11_geom ON public.trackpoints_raw_render_11 USING gist (the_geom);


--
-- Name: trackpoints_raw_render_11_track; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_render_11_track ON public.trackpoints_raw_render_11 USING btree (track_id);


--
-- Name: trackpoints_raw_render_12_geom; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_render_12_geom ON public.trackpoints_raw_render_12 USING gist (the_geom);


--
-- Name: trackpoints_raw_render_12_track; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_render_12_track ON public.trackpoints_raw_render_12 USING btree (track_id);


--
-- Name: trackpoints_raw_render_13_geom; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_render_13_geom ON public.trackpoints_raw_render_13 USING gist (the_geom);


--
-- Name: trackpoints_raw_render_13_track; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_render_13_track ON public.trackpoints_raw_render_13 USING btree (track_id);


--
-- Name: trackpoints_raw_render_14_geom; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_render_14_geom ON public.trackpoints_raw_render_14 USING gist (the_geom);


--
-- Name: trackpoints_raw_render_14_track; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_render_14_track ON public.trackpoints_raw_render_14 USING btree (track_id);


--
-- Name: trackpoints_raw_render_15_geom; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_render_15_geom ON public.trackpoints_raw_render_15 USING gist (the_geom);


--
-- Name: trackpoints_raw_render_15_track; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_render_15_track ON public.trackpoints_raw_render_15 USING btree (track_id);


--
-- Name: trackpoints_raw_render_16_geom; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_render_16_geom ON public.trackpoints_raw_render_16 USING gist (the_geom);


--
-- Name: trackpoints_raw_render_16_track; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_render_16_track ON public.trackpoints_raw_render_16 USING btree (track_id);


--
-- Name: trackpoints_raw_render_17_geom; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_render_17_geom ON public.trackpoints_raw_render_17 USING gist (the_geom);


--
-- Name: trackpoints_raw_render_17_track; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_render_17_track ON public.trackpoints_raw_render_17 USING btree (track_id);


--
-- Name: trackpoints_raw_render_18_geom; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_render_18_geom ON public.trackpoints_raw_render_18 USING gist (the_geom);


--
-- Name: trackpoints_raw_render_18_track; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_render_18_track ON public.trackpoints_raw_render_18 USING btree (track_id);


--
-- Name: trackpoints_raw_render_19_geom; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_render_19_geom ON public.trackpoints_raw_render_19 USING gist (the_geom);


--
-- Name: trackpoints_raw_render_19_track; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_render_19_track ON public.trackpoints_raw_render_19 USING btree (track_id);


--
-- Name: trackpoints_raw_render_1_geom; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_render_1_geom ON public.trackpoints_raw_render_1 USING gist (the_geom);


--
-- Name: trackpoints_raw_render_1_track; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_render_1_track ON public.trackpoints_raw_render_1 USING btree (track_id);


--
-- Name: trackpoints_raw_render_20_geom; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_render_20_geom ON public.trackpoints_raw_render_20 USING gist (the_geom);


--
-- Name: trackpoints_raw_render_20_track; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_render_20_track ON public.trackpoints_raw_render_20 USING btree (track_id);


--
-- Name: trackpoints_raw_render_21_geom; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_render_21_geom ON public.trackpoints_raw_render_21 USING gist (the_geom);


--
-- Name: trackpoints_raw_render_21_track; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_render_21_track ON public.trackpoints_raw_render_21 USING btree (track_id);


--
-- Name: trackpoints_raw_render_22_geom; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_render_22_geom ON public.trackpoints_raw_render_22 USING gist (the_geom);


--
-- Name: trackpoints_raw_render_22_track; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_render_22_track ON public.trackpoints_raw_render_22 USING btree (track_id);


--
-- Name: trackpoints_raw_render_2_geom; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_render_2_geom ON public.trackpoints_raw_render_2 USING gist (the_geom);


--
-- Name: trackpoints_raw_render_2_track; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_render_2_track ON public.trackpoints_raw_render_2 USING btree (track_id);


--
-- Name: trackpoints_raw_render_3_geom; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_render_3_geom ON public.trackpoints_raw_render_3 USING gist (the_geom);


--
-- Name: trackpoints_raw_render_3_track; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_render_3_track ON public.trackpoints_raw_render_3 USING btree (track_id);


--
-- Name: trackpoints_raw_render_4_geom; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_render_4_geom ON public.trackpoints_raw_render_4 USING gist (the_geom);


--
-- Name: trackpoints_raw_render_4_track; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_render_4_track ON public.trackpoints_raw_render_4 USING btree (track_id);


--
-- Name: trackpoints_raw_render_5_geom; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_render_5_geom ON public.trackpoints_raw_render_5 USING gist (the_geom);


--
-- Name: trackpoints_raw_render_5_track; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_render_5_track ON public.trackpoints_raw_render_5 USING btree (track_id);


--
-- Name: trackpoints_raw_render_6_geom; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_render_6_geom ON public.trackpoints_raw_render_6 USING gist (the_geom);


--
-- Name: trackpoints_raw_render_6_track; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_render_6_track ON public.trackpoints_raw_render_6 USING btree (track_id);


--
-- Name: trackpoints_raw_render_7_geom; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_render_7_geom ON public.trackpoints_raw_render_7 USING gist (the_geom);


--
-- Name: trackpoints_raw_render_7_track; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_render_7_track ON public.trackpoints_raw_render_7 USING btree (track_id);


--
-- Name: trackpoints_raw_render_8_geom; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_render_8_geom ON public.trackpoints_raw_render_8 USING gist (the_geom);


--
-- Name: trackpoints_raw_render_8_track; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_render_8_track ON public.trackpoints_raw_render_8 USING btree (track_id);


--
-- Name: trackpoints_raw_render_9_geom; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_render_9_geom ON public.trackpoints_raw_render_9 USING gist (the_geom);


--
-- Name: trackpoints_raw_render_9_track; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_render_9_track ON public.trackpoints_raw_render_9 USING btree (track_id);


--
-- Name: trackpoints_raw_temp_10_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_temp_10_index ON public.trackpoints_raw_temp_10 USING gist (the_geom);


--
-- Name: trackpoints_raw_temp_12_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_temp_12_index ON public.trackpoints_raw_temp_12 USING gist (the_geom);


--
-- Name: trackpoints_raw_temp_16_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_temp_16_index ON public.trackpoints_raw_temp_16 USING gist (the_geom);


--
-- Name: trackpoints_raw_temp_8_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trackpoints_raw_temp_8_index ON public.trackpoints_raw_temp_8 USING gist (the_geom);


--
-- Name: triangulation_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX triangulation_index ON public.triangulation USING gist (geom);


--
-- Name: user_tracks user_tracks_insert_rule; Type: RULE; Schema: public; Owner: postgres
--

CREATE RULE user_tracks_insert_rule AS
    ON INSERT TO public.user_tracks DO INSTEAD  SELECT public.user_tracks_insert_func(new.*) AS user_tracks_insert_func;


--
-- Name: user_tracks user_tracks_update_rule; Type: RULE; Schema: public; Owner: postgres
--

CREATE RULE user_tracks_update_rule AS
    ON UPDATE TO public.user_tracks DO INSTEAD  UPDATE osmapi_tables.user_tracks SET file_ref = new.file_ref, upload_state = new.upload_state, filetype = new.filetype, compression = new.compression, containertrack = new.containertrack, vesselconfigid = new.vesselconfigid, license = new.license, gauge_name = new.gauge_name, gauge = new.gauge, height_ref = new.height_ref, comment = new.comment, watertype = new.watertype, uploaddate = new.uploaddate, bbox = new.bbox, clusteruuid = new.clusteruuid, clusterseq = new.clusterseq, upr_id = COALESCE(new.upr_id, user_tracks.upr_id), num_points = new.num_points, is_container = new.is_container
  WHERE (user_tracks.track_id = old.track_id);


--
-- Name: vesselconfiguration vesselconfiguration_insert_rule; Type: RULE; Schema: public; Owner: postgres
--

CREATE RULE vesselconfiguration_insert_rule AS
    ON INSERT TO public.vesselconfiguration DO INSTEAD  SELECT public.vesselconfiguration_insert_func(new.*) AS vesselconfiguration_insert_func;


--
-- Name: vesselconfiguration vesselconfiguration_update_rule; Type: RULE; Schema: public; Owner: postgres
--

CREATE RULE vesselconfiguration_update_rule AS
    ON UPDATE TO public.vesselconfiguration DO INSTEAD  UPDATE osmapi_tables.vesselconfiguration SET name = new.name, description = new.description, mmsi = new.mmsi, manufacturer = new.manufacturer, model = new.model, loa = new.loa, breadth = new.breadth, draft = new.draft, height = new.height, displacement = new.displacement, maximumspeed = new.maximumspeed, type = new.type, upr_id = COALESCE(new.upr_id, vesselconfiguration.upr_id)
  WHERE (vesselconfiguration.id = old.id);


--
-- Name: track_info rpl_log_tif; Type: TRIGGER; Schema: osmapi_tables; Owner: postgres
--

CREATE TRIGGER rpl_log_tif AFTER INSERT OR DELETE OR UPDATE ON osmapi_tables.track_info FOR EACH ROW EXECUTE PROCEDURE public.rpl_log();


--
-- Name: user_tracks rpl_log_utr; Type: TRIGGER; Schema: osmapi_tables; Owner: postgres
--

CREATE TRIGGER rpl_log_utr AFTER INSERT OR DELETE OR UPDATE ON osmapi_tables.user_tracks FOR EACH ROW EXECUTE PROCEDURE public.rpl_log();


--
-- Name: user_tracks tub_utr; Type: TRIGGER; Schema: osmapi_tables; Owner: postgres
--

CREATE TRIGGER tub_utr BEFORE UPDATE ON osmapi_tables.user_tracks FOR EACH ROW EXECUTE PROCEDURE public.fub_utr();


--
-- Name: depthsensor depthsoffset_vesselconfigid_fkey; Type: FK CONSTRAINT; Schema: osmapi_tables; Owner: postgres
--

ALTER TABLE ONLY osmapi_tables.depthsensor
    ADD CONSTRAINT depthsoffset_vesselconfigid_fkey FOREIGN KEY (vesselconfigid) REFERENCES osmapi_tables.vesselconfiguration(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: trackgauges gauge_fkey; Type: FK CONSTRAINT; Schema: osmapi_tables; Owner: postgres
--

ALTER TABLE ONLY osmapi_tables.trackgauges
    ADD CONSTRAINT gauge_fkey FOREIGN KEY (gaugeid) REFERENCES osmapi_tables.gauge(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: gaugemeasurement gaugemeasurement_fkey; Type: FK CONSTRAINT; Schema: osmapi_tables; Owner: postgres
--

ALTER TABLE ONLY osmapi_tables.gaugemeasurement
    ADD CONSTRAINT gaugemeasurement_fkey FOREIGN KEY (gaugeid) REFERENCES osmapi_tables.gauge(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: sbassensor sbasoffset_vesselconfigid_fkey; Type: FK CONSTRAINT; Schema: osmapi_tables; Owner: postgres
--

ALTER TABLE ONLY osmapi_tables.sbassensor
    ADD CONSTRAINT sbasoffset_vesselconfigid_fkey FOREIGN KEY (vesselconfigid) REFERENCES osmapi_tables.vesselconfiguration(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: trackgauges trackgauges_trackid_fkey; Type: FK CONSTRAINT; Schema: osmapi_tables; Owner: postgres
--

ALTER TABLE ONLY osmapi_tables.trackgauges
    ADD CONSTRAINT trackgauges_trackid_fkey FOREIGN KEY (trackid) REFERENCES osmapi_tables.user_tracks(track_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: user_tracks utr_upr_fk; Type: FK CONSTRAINT; Schema: osmapi_tables; Owner: postgres
--

ALTER TABLE ONLY osmapi_tables.user_tracks
    ADD CONSTRAINT utr_upr_fk FOREIGN KEY (upr_id) REFERENCES osmapi_tables.user_profiles(id) ON DELETE CASCADE;


--
-- Name: user_tracks utr_utr_fk; Type: FK CONSTRAINT; Schema: osmapi_tables; Owner: postgres
--

ALTER TABLE ONLY osmapi_tables.user_tracks
    ADD CONSTRAINT utr_utr_fk FOREIGN KEY (containertrack) REFERENCES osmapi_tables.user_tracks(track_id);


--
-- Name: user_tracks utr_vcf_fk; Type: FK CONSTRAINT; Schema: osmapi_tables; Owner: postgres
--

ALTER TABLE ONLY osmapi_tables.user_tracks
    ADD CONSTRAINT utr_vcf_fk FOREIGN KEY (vesselconfigid) REFERENCES osmapi_tables.vesselconfiguration(id);


--
-- Name: vesselconfiguration vcf_upr_fk; Type: FK CONSTRAINT; Schema: osmapi_tables; Owner: postgres
--

ALTER TABLE ONLY osmapi_tables.vesselconfiguration
    ADD CONSTRAINT vcf_upr_fk FOREIGN KEY (upr_id) REFERENCES osmapi_tables.user_profiles(id) ON DELETE CASCADE;


--
-- Name: track_mergerun tmr_mer_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.track_mergerun
    ADD CONSTRAINT tmr_mer_fk FOREIGN KEY (mer_id) REFERENCES public.mergerun(id);


--
-- Name: track_mergerun tmr_tra_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.track_mergerun
    ADD CONSTRAINT tmr_tra_fk FOREIGN KEY (tra_id) REFERENCES osmapi_tables.user_tracks(track_id);


--
-- Name: SCHEMA osmapi_tables; Type: ACL; Schema: -; Owner: postgres
--

GRANT ALL ON SCHEMA osmapi_tables TO osm;


--
-- Name: TABLE user_profiles; Type: ACL; Schema: osmapi_tables; Owner: postgres
--

GRANT SELECT,INSERT,UPDATE ON TABLE osmapi_tables.user_profiles TO osmapi;


--
-- Name: SEQUENCE user_tracks_track_id_seq; Type: ACL; Schema: osmapi_tables; Owner: postgres
--

GRANT ALL ON SEQUENCE osmapi_tables.user_tracks_track_id_seq TO osmapi;
GRANT ALL ON SEQUENCE osmapi_tables.user_tracks_track_id_seq TO osm;


--
-- Name: TABLE user_tracks; Type: ACL; Schema: osmapi_tables; Owner: postgres
--

GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE osmapi_tables.user_tracks TO osmapi;
GRANT SELECT ON TABLE osmapi_tables.user_tracks TO osm;


--
-- Name: TABLE user_tracks; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.user_tracks TO osmapi;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.user_tracks TO osm;


--
-- Name: TABLE vesselconfiguration; Type: ACL; Schema: osmapi_tables; Owner: postgres
--

GRANT SELECT,INSERT,UPDATE ON TABLE osmapi_tables.vesselconfiguration TO osmapi;


--
-- Name: TABLE vesselconfiguration; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.vesselconfiguration TO osmapi;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.vesselconfiguration TO osm;


--
-- Name: TABLE depthsensor; Type: ACL; Schema: osmapi_tables; Owner: postgres
--

GRANT SELECT,INSERT,UPDATE ON TABLE osmapi_tables.depthsensor TO osmapi;


--
-- Name: TABLE gauge; Type: ACL; Schema: osmapi_tables; Owner: postgres
--

GRANT SELECT,INSERT,UPDATE ON TABLE osmapi_tables.gauge TO osmapi;


--
-- Name: TABLE gaugemeasurement; Type: ACL; Schema: osmapi_tables; Owner: postgres
--

GRANT SELECT,INSERT,UPDATE ON TABLE osmapi_tables.gaugemeasurement TO osmapi;


--
-- Name: TABLE license; Type: ACL; Schema: osmapi_tables; Owner: postgres
--

GRANT SELECT,INSERT,UPDATE ON TABLE osmapi_tables.license TO osmapi;


--
-- Name: TABLE sbassensor; Type: ACL; Schema: osmapi_tables; Owner: postgres
--

GRANT SELECT,INSERT,UPDATE ON TABLE osmapi_tables.sbassensor TO osmapi;


--
-- Name: SEQUENCE seq_tif; Type: ACL; Schema: osmapi_tables; Owner: postgres
--

GRANT SELECT,UPDATE ON SEQUENCE osmapi_tables.seq_tif TO osmapi;
GRANT SELECT,UPDATE ON SEQUENCE osmapi_tables.seq_tif TO osm;


--
-- Name: TABLE track_info; Type: ACL; Schema: osmapi_tables; Owner: postgres
--

GRANT ALL ON TABLE osmapi_tables.track_info TO osm;
GRANT ALL ON TABLE osmapi_tables.track_info TO osmapi;


--
-- Name: TABLE trackgauges; Type: ACL; Schema: osmapi_tables; Owner: postgres
--

GRANT SELECT,INSERT,UPDATE ON TABLE osmapi_tables.trackgauges TO osmapi;


--
-- Name: TABLE userroles; Type: ACL; Schema: osmapi_tables; Owner: postgres
--

GRANT SELECT,INSERT,UPDATE ON TABLE osmapi_tables.userroles TO osmapi;


--
-- Name: SEQUENCE vesselconfiguration_id_seq; Type: ACL; Schema: osmapi_tables; Owner: postgres
--

GRANT SELECT,UPDATE ON SEQUENCE osmapi_tables.vesselconfiguration_id_seq TO osmapi;


--
-- Name: TABLE depthsensor; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.depthsensor TO osmapi;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.depthsensor TO osm;


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
-- Name: TABLE sbassensor; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.sbassensor TO osmapi;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.sbassensor TO osm;


--
-- Name: TABLE trackpoints; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.trackpoints TO osmapi;


--
-- Name: TABLE trackpoints_empty; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.trackpoints_empty TO osmapi;


--
-- Name: TABLE trackpoints_raw_10; Type: ACL; Schema: public; Owner: postgres
--

GRANT DELETE ON TABLE public.trackpoints_raw_10 TO osmapi;


--
-- Name: TABLE trackpoints_raw_12; Type: ACL; Schema: public; Owner: postgres
--

GRANT DELETE ON TABLE public.trackpoints_raw_12 TO osmapi;


--
-- Name: TABLE trackpoints_raw_16; Type: ACL; Schema: public; Owner: postgres
--

GRANT DELETE ON TABLE public.trackpoints_raw_16 TO osmapi;


--
-- Name: TABLE trackpoints_raw_8; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT,DELETE ON TABLE public.trackpoints_raw_8 TO osmapi;


--
-- Name: TABLE trackpoints_raw_filter_10; Type: ACL; Schema: public; Owner: postgres
--

GRANT DELETE ON TABLE public.trackpoints_raw_filter_10 TO osmapi;


--
-- Name: TABLE trackpoints_raw_filter_12; Type: ACL; Schema: public; Owner: postgres
--

GRANT DELETE ON TABLE public.trackpoints_raw_filter_12 TO osmapi;


--
-- Name: TABLE trackpoints_raw_filter_16; Type: ACL; Schema: public; Owner: postgres
--

GRANT DELETE ON TABLE public.trackpoints_raw_filter_16 TO osmapi;


--
-- Name: TABLE trackpoints_raw_filter_8; Type: ACL; Schema: public; Owner: postgres
--

GRANT DELETE ON TABLE public.trackpoints_raw_filter_8 TO osmapi;


--
-- Name: TABLE trackpoints_raw_temp_10; Type: ACL; Schema: public; Owner: postgres
--

GRANT DELETE ON TABLE public.trackpoints_raw_temp_10 TO osmapi;


--
-- Name: TABLE trackpoints_raw_temp_12; Type: ACL; Schema: public; Owner: postgres
--

GRANT DELETE ON TABLE public.trackpoints_raw_temp_12 TO osmapi;


--
-- Name: TABLE trackpoints_raw_temp_16; Type: ACL; Schema: public; Owner: postgres
--

GRANT DELETE ON TABLE public.trackpoints_raw_temp_16 TO osmapi;


--
-- Name: TABLE trackpoints_raw_temp_8; Type: ACL; Schema: public; Owner: postgres
--

GRANT DELETE ON TABLE public.trackpoints_raw_temp_8 TO osmapi;


--
-- Name: TABLE user_profiles; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.user_profiles TO osmapi;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.user_profiles TO osm;


--
-- PostgreSQL database dump complete
--

