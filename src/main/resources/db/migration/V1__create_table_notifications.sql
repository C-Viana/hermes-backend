CREATE TABLE IF NOT EXISTS notifications (
  id UUID NOT NULL,
  addressee VARCHAR(80)[] NOT NULL,
  message text NOT NULL,
  type VARCHAR(20) NOT NULL,
  status VARCHAR(15) NOT NULL,
  date_schedule TIMESTAMP,
  PRIMARY KEY (id)
);