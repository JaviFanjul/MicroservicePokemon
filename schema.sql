-- schema.sql
-- Script para crear la tabla "pokemon" en PostgreSQL

CREATE TABLE IF NOT EXISTS pokemon (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    npokedex INTEGER NOT NULL,
    types TEXT[] NOT NULL,
    weakagainst TEXT[] NOT NULL,
    speed INTEGER NOT NULL,
    attack INTEGER[] NOT NULL,
    defense INTEGER[] NOT NULL
);

-- Ejecutar una vez instalado postgres
-- psql -U postgres -d pokemon_db -f schema.sql