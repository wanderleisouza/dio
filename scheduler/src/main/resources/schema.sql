DROP TABLE IF EXISTS client_route; -- only doing this for demo purposes and to clean everything on restart

CREATE TABLE client_route (
                              id varchar(255) NOT NULL,
                              company varchar(255) NOT NULL,
                              source_folder varchar(255) NOT NULL,
                              target_folder varchar(255) NOT NULL,
                              cloud_id int NOT NULL,
                              extension_type varchar(255) NOT NULL,
                              processing_type varchar(255) NOT NULL,
                              access_key_id varchar(255) NOT NULL,
                              secret_access_key varchar(255) NOT NULL,
                              active boolean NOT NULL,
                              PRIMARY KEY (id)
);
