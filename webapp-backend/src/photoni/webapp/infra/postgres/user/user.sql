-- :name create-users-table
-- :command :execute
-- :result :raw
create table if not exists users (
  user_id UUID PRIMARY KEY NOT NULL DEFAULT uuid_generate_v1(),
  name text,
  title text,
  email text,
  role text,
  age int,
  updated_by text,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
)

-- :name upsert-user :<!
INSERT INTO "users" (user_id, name, title, email, role, age, updated_by)
    VALUES (:user-id, :name, :title, :email, :role, :age, :updated-by)
    ON CONFLICT (user_id)
    DO UPDATE SET
        name = :name,
        title = :title,
        email = :email,
        role = :role,
        age = :age,
        updated_by = :updated-by;

-- :name select-users :? :*
SELECT * FROM "users";

-- :name select-user-by-id :? :1
SELECT * FROM "users" WHERE user_id = :user-id;

-- :name delete-user-by-id :? :1
DELETE FROM "users" WHERE user_id = :user-id;









/* ...snip... */

-- A :result value of :n below will return affected rows:
-- :name insert-character :! :n
-- :doc Insert a single character returning affected row count
insert into characters (name, specialty)
values (:name, :specialty)

-- :name insert-characters :! :n
-- :doc Insert multiple characters with :tuple* parameter type
insert into characters (name, specialty)
values :tuple*:characters

/* ...snip... */

-- A ":result" value of ":1" specifies a single record
-- (as a hashmap) will be returned
-- :name character-by-id :? :1
-- :doc Get character by id
select * from characters
where id = :id

-- Let's specify some columns with the
-- identifier list parameter type :i* and
-- use a value list parameter type :v* for IN()
-- :name characters-by-ids-specify-cols :? :*
-- :doc Characters with returned columns specified
select :i*:cols from characters
where id in (:v*:ids)