INSERT INTO public.user_profiles (             user_name,                                   password, salt, attempts, last_attempt, forename,     surname, country, language, organisation,                  phone, acceptedemailcontact, id) 
                          VALUES ('volkmann@devpool.net', '7c222fb2927d828af22f592134e8932480637c0d', NULL,        0,         NULL, 'Steffen', 'Volkmann',    'DE',     'de', 'OpenSeaMap', 'volkmann@devpool.net',                 true, 1000);


INSERT INTO public.vesselconfiguration (id,      name,  description,              user_name,      mmsi, manufacturer, model,  loa, breadth, draft, height, displacement, maximumspeed, type, upr_id) 
                                VALUES (1, 'Fratello', 'sport boat', 'volkmann@devpool.net', 'MMSI001',        'VEHA', '35', 10.0,     3.0,   3.5,   10.0,         10.0,            1, NULL, 1000);

INSERT INTO public.depthsensor (vesselconfigid, x,     y,   z, sensorid,    manufacturer, model, frequency, angleofbeam, offsetkeel, offsettype, id) 
                         VALUES (1,             0.00, 0.0, 0.0, NULL, 'Echopilot', 'Bronze', 0, 0, 0.5, 'transducer', 1);


INSERT INTO public.license (name,                                                     shortname, text,  public, id, user_name) 
                    VALUES ('Open Data Commons Public Domain Dedication and License', 'PDDL',   'd.b.d.', true,    1, '');

INSERT INTO public.user_tracks (track_id,              user_name,       file_ref, upload_state, filetype,    compression, containertrack, vesselconfigid, license, gauge_name, gauge, height_ref, comment, watertype, uploaddate,   bbox, clusteruuid, clusterseq, upr_id, num_points, is_container) 
                        VALUES (  124299, 'volkmann@devpool.net', 'DATA0001.DAT',            1,      ' ', 'DATA0001.DAT',           NULL,              1,       1,       NULL,  NULL,       NULL,    NULL,      NULL,        NULL,  NULL,        NULL,       NULL,   NULL,       NULL,         NULL);

INSERT INTO public.user_tracks (track_id,              user_name,       file_ref, upload_state, filetype,    compression, containertrack, vesselconfigid, license, gauge_name, gauge, height_ref, comment, watertype, uploaddate,   bbox, clusteruuid, clusterseq, upr_id, num_points, is_container) 
                        VALUES (  135695, 'volkmann@devpool.net', 'DATA0001.DAT',            1,      ' ', 'DATA0002.DAT',           NULL,              1,       1,       NULL,  NULL,       NULL,    NULL,      NULL,        NULL,  NULL,        NULL,       NULL,   NULL,       NULL,         NULL);

