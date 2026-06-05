create table if not exists notifications (
  id uuid,
  addressee varchar(80) array not null,
  message clob not null,
  type varchar(20) not null,
  status varchar(15) not null,
  date_schedule timestamp,
  
  primary key (id)
)