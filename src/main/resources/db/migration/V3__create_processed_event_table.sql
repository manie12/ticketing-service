CREATE TABLE IF NOT EXISTS ticketing.processed_event (
                                                         id         VARCHAR(255) PRIMARY KEY,
    processed_at TIMESTAMPTZ NOT NULL DEFAULT now()
    );
