drop TABLE  filemgr.file_uploads;

CREATE TABLE IF NOT EXISTS filemgr.file_uploads (
  id INT NOT NULL AUTO_INCREMENT,  
  user_first_name VARCHAR(40) NOT NULL,
  user_last_name VARCHAR(40) NOT NULL,
  file_name VARCHAR(40) NOT NULL,
  file_desc VARCHAR(100) ,
  creation_date VARCHAR(20) NOT NULL,
  last_updated_date VARCHAR(20) NOT NULL,
  PRIMARY KEY (id));