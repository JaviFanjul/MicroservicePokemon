-- schema.sql
-- Script para crear la tabla "pokemon" en PostgreSQL

CREATE TABLE IF NOT EXISTS pokemon (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    nPokedex INTEGER NOT NULL,
    weakAgainst TEXT[] NOT NULL,
    speed INTEGER NOT NULL,
    attack INTEGER NOT NULL,
    defense INTEGER NOT NULL
);

-- Ejecutar una vez instalado postgres
-- psql -U postgres -d pokemon_db -f schema.sql