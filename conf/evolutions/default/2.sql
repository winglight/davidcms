# --- !Ups

ALTER TABLE content_model ADD sms_number varchar(255);

# --- !Downs

ALTER TABLE content_model DROP sms_number;