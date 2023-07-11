insert into place (id, name, description, region, address) values(nextval('place_seq'), 'Casa Mia', 'Molto bella!', 'Lazio','via di casa mia');
insert into place (id, name, description, region, address) values(nextval('place_seq'), 'Casa Tua', 'Molto Carina!', 'Piemonte','via di casa tua');
insert into place (id, name, description, region, address) values(nextval('place_seq'), 'Terzo Luogo', 'Dedicato al terzo luogo', 'Toscana', 'via quellali');
insert into place (id, name, description, region, address) values(nextval('place_seq'),'Faro Fiumicino', 'Alla portata di tutti, ottimo luogo per iniziare', 'Lazio','via faro di fiumicino');


insert into photo(id,filename,image_data) values(nextval('photo_seq'),'MercatiGenerali','/images/IndexSlideshow/2');
insert into photo(id,filename,image_data) values(nextval('photo_seq'),'sanLucaBO','/images/sanLucaBO');
insert into photo(id,filename,image_data) values(nextval('photo_seq'),'casaccio','/images/begin_carousel');
insert into photo(id,filename,image_data) values(nextval('photo_seq'),'faroFiumicino','/images/faroFiumicino');