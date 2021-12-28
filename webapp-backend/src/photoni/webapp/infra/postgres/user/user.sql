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

-- :name upsert-user :insert :raw
INSERT INTO "users" (user_id, name, title, email, role, age, updated_by)
    VALUES (:user_id, :name, :title, :email, :role, :age, :updated_by)
    ON CONFLICT (user_id)
    DO UPDATE SET
        name = :name,
        title = :title,
        email = :email,
        role = :role,
        age = :age,
        updated_by = :updated_by;

-- :name select-users :? :*
SELECT * FROM "users";

-- :name select-user-by-id :? :1
SELECT * FROM "users" WHERE user_id = :user_id;

-- :name delete-user-by-id :! :n
DELETE FROM "users" WHERE user_id = :user_id;
