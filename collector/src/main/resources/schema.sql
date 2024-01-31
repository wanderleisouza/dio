CREATE TABLE IF NOT EXISTS file_control (
    id varchar(255) NOT NULL,
    trace_id varchar(255) NOT NULL,
    route_id varchar(255) NOT NULL,
    filename varchar(255) NOT NULL,
    copied_at timestamp NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS scan_control (
    route_id varchar(255) NOT NULL,
    scanned_at timestamp NOT NULL,
    PRIMARY KEY (route_id)
);
