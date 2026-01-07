CREATE TABLE IF NOT EXISTS user_locations (
                                              id SERIAL PRIMARY KEY,
                                              location geometry(Point, 4326),
                                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                              updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create spatial index for better performance
CREATE INDEX IF NOT EXISTS idx_user_locations_geom
    ON user_locations USING GIST(location);

-- Create index on user_id
CREATE INDEX IF NOT EXISTS idx_user_locations_user_id
    ON user_locations(id);
