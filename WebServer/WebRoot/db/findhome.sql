-- Create table ‘FHUSER’
create table FHUSER
(
  userid   NUMBER(8) not null,
  username VARCHAR2(16) not null,
  password VARCHAR2(32) not null,
  model    VARCHAR2(10) not null,
  realname     VARCHAR2(10) not null,
  sex      VARCHAR2(6) not null,
  tel      VARCHAR2(15) not null,
  collect  VARCHAR2(50),
  record   VARCHAR2(50),
  question VARCHAR2(50) not null,
  answer   VARCHAR2(20) not null
  userimgpath VARCHAR2(50)
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255;
-- Create/Recreate primary, unique and foreign key constraints 
alter table FHUSER
  add constraint PK_FHUSER primary key (USERID)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255;
  
  ---------------PROPERTY--------------------------------------------------
  -- Create table
create table PROPERTY
(
  propertyno       NUMBER(8) not null,
  userid           NUMBER(8) not null,
  status           VARCHAR2(10),
  address          VARCHAR2(50),
  homesize         VARCHAR2(20),
  photo            VARCHAR2(200),
  price            VARCHAR2(10),
  lease_trem       VARCHAR2(10),
  start_lease_date VARCHAR2(20),
  expire_date      VARCHAR2(20),
  total_rent       VARCHAR2(10),
  deposit          VARCHAR2(10),
  propertyname     VARCHAR2(10) not null,
  score            NUMBER(3,1)
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255;
-- Create/Recreate primary, unique and foreign key constraints 
alter table PROPERTY
  add constraint PK_PROPERTY primary key (PROPERTYNO)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255;
alter table PROPERTY
  add constraint FK_USERID foreign key (USERID)
  references FHUSER (USERID);

  
  ---------------FACILITY---------------------------------------------------------
  -- Create table
create table FACILITY
(
  propertyid		   NUMBER(8) not null,
  propertyno           NUMBER(8) not null,
  tv               CHAR(1),
  air_conditioning CHAR(1),
  washer           CHAR(1),
  network          CHAR(1),
  computer         CHAR(1),
  dryer            CHAR(1)
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255;
-- Create/Recreate primary, unique and foreign key constraints 
alter table FACILITY
  add constraint PK_FACILITY primary key (PROPERTYID)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255;
  alter table FACILITY
  add constraint FK_PROPERTYNO foreign key (PROPERTYNO)
  references PROPERTY (PROPERTYNO);
  
  
------------------------CITY----------------------------------------------
  
  
-- Create table
create table CITY
(
  cityid   NUMBER(8) not null,
  cityname VARCHAR2(20) not null,
  citypath VARCHAR2(50)
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64
    next 1
    minextents 1
    maxextents unlimited
  );
-- Create/Recreate primary, unique and foreign key constraints 
alter table CITY
  add constraint PK_CITY primary key (CITYID)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255;
  
----------------------------------------------------------------- 

  -- Create sequence 
create sequence SEQ_FHUSER
minvalue 1
maxvalue 999999
start with 1
increment by 1
cache 20
order;

----------------------------------------------------------------------
-- Create sequence 
create sequence SEQ_PROPERTY
minvalue 1
maxvalue 999999
start with 1
increment by 1
cache 20
order;


---------------------------------------------------------
-- Create sequence 
create sequence SEQ_FACILITY
minvalue 1
maxvalue 999999
start with 1
increment by 1
cache 20
order;

------------------------------------------------------------
-- Create sequence 
create sequence SEQ_CITY
minvalue 1
maxvalue 999999
start with 1
increment by 1
cache 20
order;
---------------------------------------------------
insert into fhuser(id,username,password,model,realname,sex,
  tel,collect,record,question,answer)
values(seq_fhuser.nextval,'admin',
  '21232f297a57a5a743894a0e4a801fc3','普通用户'，'彭浩南','男','133456789011',null,null,'你的兴趣是什么？','听歌');
  
  
insert into city (cityid,cityname,citypath) values (seq_city.nextval,'长沙','images/city_cs.jpg') ;
  select * from city
  
  update city set citypath='images/city/cs.jpg' where cityname='长沙'

insert into city  values (seq_city.nextval,'杭州','images/city/hz.jpg') ;
insert into city  values (seq_city.nextval,'上海','images/city/sh.jpg') ;
insert into city  values (seq_city.nextval,'香港','images/city/xg.jpg') ;
insert into city  values (seq_city.nextval,'北京','images/city/bj.jpg') ;



insert into property
values(seq_property.nextval,8,'待租','长沙市芙蓉区五一大道','单间','images/city/cs.jpg',300.00,0,0,0,0,0,'小小雅居',3.5);
insert into property
values(seq_property.nextval,8,'待租','长沙市芙蓉区文运街','一室一厅','images/city/cs.jpg',400.00,0,0,0,0,0,'文运居',4.0);
insert into property
values(seq_property.nextval,8,'待租','长沙市天心区新开铺街道','两室一厅','images/city/cs.jpg',355.00,0,0,0,0,0,'开铺好居',4.5);
insert into property
values(seq_property.nextval,11,'待租','长沙市天心区青园街道','一室一厅','images/city/cs.jpg',288.00,0,0,0,0,0,'青园小居',4.0);
insert into property
values(seq_property.nextval,11,'待租','长沙市芙蓉区文运街','两室一厅','images/city/cs.jpg',432.00,0,0,0,0,0,'文运小苑',4.0);
commit

--collect---------------------------------------

-- Create table
create table collect
(
  id         number(8) not null,
  username   varchar2(10),
  userid     varchar2(10),
  housename  varchar2(20),
  houseno    varchar2(10),
  userimg    varchar2(20),
  housephoto varchar2(20),
  score      varchar2(10),
  price      number(5,1)
)
;
-- Create/Recreate primary, unique and foreign key constraints 
alter table collect
  add constraint PK_COLLECT primary key (ID);
  
  
