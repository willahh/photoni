-- :name create-group-table
-- :command :execute
-- :result :raw
create table if not exists "group" (
  group_id UUID PRIMARY KEY NOT NULL DEFAULT uuid_generate_v1(),
  name text,
  updated_by text,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
)

-- :name upsert-group :insert :raw
INSERT INTO "group" (group_id, name, updated_by)
    VALUES (:group_id, :name, :updated_by)
    ON CONFLICT (group_id)
    DO UPDATE SET
        name = :name,
        updated_by = :updated_by;

-- :name select-groups :? :*
SELECT * FROM "group";

-- :name select-group-by-id :? :1
SELECT * FROM "group" WHERE group_id = :group_id;

-- :name delete-group-by-id :! :n
DELETE FROM "group" WHERE group_id = :group_id;
