# noinspection SqlResolveForFile
-- noinspection SqlNoDataSourceInspectionForFile

use prod;
ALTER table ticket
    ADD DISCOUNT bool;
commit;

use test;
ALTER table ticket
    ADD DISCOUNT bool;
commit;