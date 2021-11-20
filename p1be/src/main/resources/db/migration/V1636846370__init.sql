create table "limitation"
(
    "limitation_id"      uuid    not null primary key,
    "period_seconds"     numeric not null,
    "limitation_seconds" numeric not null,
    "domain"             text    not null
);

create table "report"
(
    "report_id"               uuid      not null primary key,
    "url"                     text      not null,
    "reported_at"             timestamp not null,
    "report_interval_seconds" numeric   not null
)