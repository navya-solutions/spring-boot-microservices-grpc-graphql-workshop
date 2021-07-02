-- Script exectuted when the container is initialization

-- This script create the app user and the schemas needed for the apps

CREATE USER sample WITH PASSWORD 'sample';

CREATE SCHEMA sampleDB AUTHORIZATION sample;

commit;
