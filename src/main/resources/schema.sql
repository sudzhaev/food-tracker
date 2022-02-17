CREATE TABLE food_track
(
	id SERIAL NOT NULL
		CONSTRAINT food_track_pk PRIMARY KEY,
	chat_id INT NOT NULL,
	name VARCHAR NOT NULL,
	calories INT NOT NULL,
	created_at TIMESTAMP DEFAULT now() NOT NULL
);
