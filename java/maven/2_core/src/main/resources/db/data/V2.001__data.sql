INSERT INTO T02_PERMISSION_TYPE (C02_PERMISSION_TYPE_ID, C02_NAME)
                         select 1, 'CRUD_USERS'     from dual
               union all select 2, 'CRUD_ALL_TRIPS' from dual
               union all select 3, 'CRUD_OWN_TRIPS' from dual
;

INSERT INTO T01_USER (C01_USER_ID, C01_USERNAME, C01_PASSWORD, C01_FIRSTNAME, C01_LASTNAME, C01_EMAIL, C01_ENABLED)
          select 1, 'admin',       '$2a$08$lDnHPz7eUkSi6ao14Twuau08mzhWrL4kyZGGU5xfiGALO/Vxd5DOi', 'Admin',       'Admin',       'admin@tp.com'       , 1 from dual
union all select 2, 'usermanager', '$2a$10$lM1sDaT7OZshX0sufeNtuuSJfVzWVUqXujc3CjXqR.YtLWl2JTkKO', 'UserManager', 'UserManager', 'usermanager@tp.com' , 1 from dual
union all select 3, 'user1',       '$2a$10$lM1sDaT7OZshX0sufeNtuuSJfVzWVUqXujc3CjXqR.YtLWl2JTkKO', 'User1',       'User1',       'user1@tp.com'       , 1 from dual
union all select 4, 'user2',       '$2a$10$lM1sDaT7OZshX0sufeNtuuSJfVzWVUqXujc3CjXqR.YtLWl2JTkKO', 'User2',       'User2',       'user2@tp.com'       , 1 from dual
union all select 5, 'disabled',    '$2a$10$lM1sDaT7OZshX0sufeNtuuSJfVzWVUqXujc3CjXqR.YtLWl2JTkKO', 'Disabled',    'Disabled',    'disabled@tp.com'    , 0 from dual
;

INSERT INTO T03_PERMISSION (C03_USER_ID, C03_PERMISSION_TYPE_ID)
          select 1, 1 from dual
union all select 1, 2 from dual
union all select 1, 3 from dual
union all select 2, 1 from dual
union all select 3, 3 from dual
union all select 4, 3 from dual
;

INSERT INTO T04_TRIP (C04_TRIP_ID, C04_USER_ID, C04_DESTINATION, C04_START_DATE, C04_END_DATE, C04_COMMENT)
          select 1,  4, 'Destination 1', TO_DATE('2017/01/01', 'YYYY/MM/DD'), TO_DATE('2017/01/11', 'YYYY/MM/DD'), 'Comment 1' from dual
union all select 2,  4, 'Destination 2', TO_DATE('2017/01/02', 'YYYY/MM/DD'), TO_DATE('2017/01/12', 'YYYY/MM/DD'), 'Comment 2' from dual
union all select 3,  4, 'Destination 3', TO_DATE('2017/01/03', 'YYYY/MM/DD'), TO_DATE('2017/01/13', 'YYYY/MM/DD'), 'Comment 3' from dual
union all select 4,  4, 'Destination 4', TO_DATE('2017/01/04', 'YYYY/MM/DD'), TO_DATE('2017/01/14', 'YYYY/MM/DD'), 'Comment 4' from dual
union all select 5,  3, 'Dubai, United Arab Emirates', TO_DATE('2016/01/17', 'YYYY/MM/DD'), TO_DATE('2016/01/19', 'YYYY/MM/DD'), 'Intersec-The World''s Leading Trade Fair for Security, Safety and Fire Protection' from dual
union all select 6,  3, 'Malmo, Sweden', TO_DATE('2016/02/10', 'YYYY/MM/DD'), TO_DATE('2016/02/11', 'YYYY/MM/DD'), 'SKYDD & SÄKERHET Syd-Safety and Security Exhibition' from dual
union all select 7,  3, 'Madrid, Spain', TO_DATE('2016/02/23', 'YYYY/MM/DD'), TO_DATE('2016/02/26', 'YYYY/MM/DD'), 'SICUR-International Security, Safety and Fire Protection Trade Fair' from dual
union all select 8,  3, 'Istanbul, Turkey', TO_DATE('2016/02/25', 'YYYY/MM/DD'), TO_DATE('2016/02/27', 'YYYY/MM/DD'), 'SeSa Build-Trade Fair for Security, Safety, Building Construction and IT Solutions' from dual
union all select 9,  3, 'Bolzano, Italy', TO_DATE('2016/02/26', 'YYYY/MM/DD'), TO_DATE('2016/02/28', 'YYYY/MM/DD'), 'CIVIL PROTECT-International Tradeshow for Civil Protection, Fire Prevention and Emergency' from dual
union all select 10, 3, 'Krasnodar, Russia', TO_DATE('2016/03/01', 'YYYY/MM/DD'), TO_DATE('2016/03/04', 'YYYY/MM/DD'), 'Securika Krasnodar-Exhibition of Security and Fire Protection Equipment and Products' from dual
union all select 11, 3, 'Kiev, Ukraine', TO_DATE('2016/03/02', 'YYYY/MM/DD'), TO_DATE('2016/03/04', 'YYYY/MM/DD'), 'KIPS-International Exhibition for Protection, Security and Fire Safety' from dual
union all select 12, 3, 'Moscow, Russia', TO_DATE('2016/03/14', 'YYYY/MM/DD'), TO_DATE('2016/03/17', 'YYYY/MM/DD'), 'MIPS / Securika-Moscow International Exhibition for Protection, Security and Fire Safety' from dual
union all select 13, 3, 'Sao Paulo, Brazil', TO_DATE('2016/03/15', 'YYYY/MM/DD'), TO_DATE('2016/03/17', 'YYYY/MM/DD'), 'ISC BRASIL-International Electronic Security Exposition and Conference' from dual
union all select 14, 3, 'Las Vegas, USA', TO_DATE('2016/04/06', 'YYYY/MM/DD'), TO_DATE('2016/04/08', 'YYYY/MM/DD'), 'ISC West-International Security Conference and Exposition West' from dual
union all select 15, 3, 'Almaty, Kazakhstan', TO_DATE('2016/04/13', 'YYYY/MM/DD'), TO_DATE('2016/04/15', 'YYYY/MM/DD'), 'AIPS + ITS-International Exhibition for Security, Protection, Fire Safety and Rescue / International Information Technologies, Security and Communication Exhibition' from dual
union all select 16, 3, 'Poznan, Poland', TO_DATE('2016/04/25', 'YYYY/MM/DD'), TO_DATE('2016/04/28', 'YYYY/MM/DD'), 'SECUREX-International Security Exhibition' from dual
union all select 17, 3, 'Toronto, Canada', TO_DATE('2016/04/26', 'YYYY/MM/DD'), TO_DATE('2016/04/27', 'YYYY/MM/DD'), 'Partners in Prevention-Health and Safety Canada Conference and Trade Show' from dual
union all select 18, 3, 'Poznan, Poland', TO_DATE('2016/04/26', 'YYYY/MM/DD'), TO_DATE('2016/04/28', 'YYYY/MM/DD'), 'SAWO-International Fair of Work Protection, Fire-Fighting and Rescue Equipment' from dual
union all select 19, 3, 'Mexico City, Mexico', TO_DATE('2016/04/26', 'YYYY/MM/DD'), TO_DATE('2016/04/28', 'YYYY/MM/DD'), 'Expo Seguridad México powered by ISC-International Exhibition for Security' from dual
union all select 20, 3, 'Mexico City, Mexico', TO_DATE('2016/04/26', 'YYYY/MM/DD'), TO_DATE('2016/04/28', 'YYYY/MM/DD'), 'NFPA - Mexico Fire Expo-Mexico Fire and Safety Exposition' from dual
union all select 21, 3, 'Mexico City, Mexico', TO_DATE('2016/04/26', 'YYYY/MM/DD'), TO_DATE('2016/04/28', 'YYYY/MM/DD'), 'Expo Seguridad Industrial México-Exhibition for Professionals Involved in Health and Occupational Safety in Mexico' from dual
union all select 22, 3, 'Astana, Kazakhstan', TO_DATE('2016/04/27', 'YYYY/MM/DD'), TO_DATE('2016/04/28', 'YYYY/MM/DD'), 'KIOSH-Kazakhstan International Occupational Safety and Health Conference and Exhibition' from dual
union all select 23, 3, 'Daegu, Korea, Republic', TO_DATE('2016/04/27', 'YYYY/MM/DD'), TO_DATE('2016/04/29', 'YYYY/MM/DD'), 'Fire EXPO-International Fire & Safety Expo Korea' from dual
union all select 24, 3, 'Hong Kong, Hong Kong', TO_DATE('2016/05/04', 'YYYY/MM/DD'), TO_DATE('2016/05/06', 'YYYY/MM/DD'), 'Asian Securitex-Asian International Security, Safety and Fire Protection Show and Conference' from dual
union all select 25, 3, 'Istanbul, Turkey', TO_DATE('2016/05/08', 'YYYY/MM/DD'), TO_DATE('2016/05/11', 'YYYY/MM/DD'), 'TOS+H Expo-Turkish Occupational Safety + Health Exhibition' from dual
union all select 26, 3, 'Sao Paulo, Brazil', TO_DATE('2016/05/10', 'YYYY/MM/DD'), TO_DATE('2016/05/12', 'YYYY/MM/DD'), 'EXPOSEC-International Security Show' from dual
union all select 27, 3, 'Dammam, Saudi Arabia', TO_DATE('2016/05/16', 'YYYY/MM/DD'), TO_DATE('2016/05/18', 'YYYY/MM/DD'), 'SSS - Saudi Safety & Security-Saudi International Exhibition for Fire, Safety and Security' from dual
union all select 28, 3, 'Johannesburg, South Africa', TO_DATE('2016/05/24', 'YYYY/MM/DD'), TO_DATE('2016/05/26', 'YYYY/MM/DD'), 'Securex South Africa-International Commercial Security, Homeland Security and Fire Exhibition' from dual
union all select 29, 3, 'Krasnoyarsk, Russia', TO_DATE('2016/05/25', 'YYYY/MM/DD'), TO_DATE('2016/05/27', 'YYYY/MM/DD'), 'Modern security systems - Anti-terror-Specialized Forum and Fair for Safety, Security, Fire Prevention, Defense, Military and Criminalistical Technique, Emergency Equipment' from dual
union all select 30, 3, 'Lima, Peru', TO_DATE('2016/05/26', 'YYYY/MM/DD'), TO_DATE('2016/05/28', 'YYYY/MM/DD'), 'SEGURITEC PERU-International Trade Fair for Security, Safety, Fire, Rescue and Police Equipment' from dual
union all select 31, 3, 'Berlin, Germany', TO_DATE('2016/06/01', 'YYYY/MM/DD'), TO_DATE('2016/06/04', 'YYYY/MM/DD'), 'ILA-Berlin Air Show' from dual
union all select 32, 3, 'Guangzhou, China, PR', TO_DATE('2016/06/13', 'YYYY/MM/DD'), TO_DATE('2016/06/15', 'YYYY/MM/DD'), 'CFE-China (Guangzhou) International Fire Safety Exhibition' from dual
union all select 33, 3, 'Ho Chi Minh City, Vietnam', TO_DATE('2016/06/14', 'YYYY/MM/DD'), TO_DATE('2016/06/16', 'YYYY/MM/DD'), 'SECURITY & FIRE VIETNAM-International Security Systems, Fire Protection Equipment and Technology Exhibition' from dual
union all select 34, 3, 'London, United Kingdom', TO_DATE('2016/06/21', 'YYYY/MM/DD'), TO_DATE('2016/06/23', 'YYYY/MM/DD'), 'IFSEC International-The Global Event for the Security Industry' from dual
union all select 35, 3, 'Berne, Switzerland', TO_DATE('2016/06/22', 'YYYY/MM/DD'), TO_DATE('2016/06/24', 'YYYY/MM/DD'), 'ArbeitsSicherheit Schweiz-Trade Fair for Occupational Safety, Health Protection & Health Promotion in the Workplace' from dual
union all select 36, 3, 'Munich, Germany', TO_DATE('2016/07/06', 'YYYY/MM/DD'), TO_DATE('2016/07/07', 'YYYY/MM/DD'), 'SicherheitsExpo-Trade Fair for Entrance Control, CCTV, Fire Prevention and Perimeter Protection' from dual
union all select 37, 3, 'Nairobi, Kenya', TO_DATE('2016/07/12', 'YYYY/MM/DD'), TO_DATE('2016/07/14', 'YYYY/MM/DD'), 'SecProTec East Africa powered by Intersec-East and Central Africa''s Trade Fair for the Security and Protection Industry' from dual
union all select 38, 3, 'Curitiba, Brazil', TO_DATE('2016/08/10', 'YYYY/MM/DD'), TO_DATE('2016/08/12', 'YYYY/MM/DD'), 'PREVENSUL-Occupational Health and Safety Trade Show' from dual
union all select 39, 3, 'Tinley Park, USA', TO_DATE('2016/08/16', 'YYYY/MM/DD'), TO_DATE('2016/08/17', 'YYYY/MM/DD'), 'Midwest Security and Police Conference and Expo' from dual
union all select 40, 3, 'San Antonio, USA', TO_DATE('2016/08/19', 'YYYY/MM/DD'), TO_DATE('2016/08/20', 'YYYY/MM/DD'), 'Fire-Rescue International-Exhibition' from dual
union all select 41, 3, 'Singapore, Singapore', TO_DATE('2016/08/24', 'YYYY/MM/DD'), TO_DATE('2016/08/26', 'YYYY/MM/DD'), 'OS+H Asia-Occupational Safety + Health Exhibition for Asia' from dual
union all select 42, 3, 'Brisbane, Australia', TO_DATE('2016/08/30', 'YYYY/MM/DD'), TO_DATE('2016/09/01', 'YYYY/MM/DD'), 'AFAC powered by INTERSCHUTZ-Fire Fighting, Emergency Services and Public Safety Conference and Trade Exhibition' from dual
union all select 43, 3, 'Kielce, Poland', TO_DATE('2016/09/06', 'YYYY/MM/DD'), TO_DATE('2016/09/09', 'YYYY/MM/DD'), 'LOGISTYKA-International Logistics Fair for Defence, Army and Rescue Services' from dual
union all select 44, 3, 'Buenos Aires, Argentina', TO_DATE('2016/09/07', 'YYYY/MM/DD'), TO_DATE('2016/09/09', 'YYYY/MM/DD'), 'Intersec Buenos Aires-International Fair of Security, Fire Protection, Electronic Security, Industrial Security and Personal Protection' from dual
union all select 45, 3, 'Jyvaskyla, Finland', TO_DATE('2016/09/07', 'YYYY/MM/DD'), TO_DATE('2016/09/09', 'YYYY/MM/DD'), 'Turvallisuus-Safety, Security and Rescue Exhibition' from dual
union all select 46, 3, 'Bratislava, Slovak Republic', TO_DATE('2016/09/07', 'YYYY/MM/DD'), TO_DATE('2016/09/09', 'YYYY/MM/DD'), 'Security Bratislava-International Fair of Security Technology, Information Security, Fire Prevention and Rescue Systems' from dual
union all select 47, 3, 'Tampere, Finland', TO_DATE('2016/09/13', 'YYYY/MM/DD'), TO_DATE('2016/09/15', 'YYYY/MM/DD'), 'EuroSafety-International Trade Fair for the Safety Industry' from dual
union all select 48, 3, 'Izhevsk, Russia', TO_DATE('2016/09/14', 'YYYY/MM/DD'), TO_DATE('2016/09/16', 'YYYY/MM/DD'), 'Integrated Security-Security and Safety Trade Fair' from dual
union all select 49, 3, 'Prague, Czech Republic', TO_DATE('2016/09/20', 'YYYY/MM/DD'), TO_DATE('2016/09/24', 'YYYY/MM/DD'), 'FSDays-Prague Fire and Security Days' from dual
union all select 50, 3, 'Gornja Radgona, Slovenia', TO_DATE('2016/09/22', 'YYYY/MM/DD'), TO_DATE('2016/09/24', 'YYYY/MM/DD'), 'SOBRA-International Fair of Defence, Security, Protection and Rescue' from dual
union all select 51, 3, 'Islamabad, Pakistan', TO_DATE('2016/09/27', 'YYYY/MM/DD'), TO_DATE('2016/09/29', 'YYYY/MM/DD'), 'Safety & Security Pakistan-International Rescue, Safety and Security Exhibition & Conference' from dual
union all select 52, 3, 'Essen, Germany', TO_DATE('2016/09/27', 'YYYY/MM/DD'), TO_DATE('2016/09/30', 'YYYY/MM/DD'), 'Security Essen-World Forum for Security and Fire Protection' from dual
union all select 53, 3, 'Singapore, Singapore', TO_DATE('2016/09/28', 'YYYY/MM/DD'), TO_DATE('2016/09/30', 'YYYY/MM/DD'), 'Fire & Disaster Asia-International Exhibition for Fire Fighting Equipment and Disaster Management Technologies and Services' from dual
union all select 54, 3, 'Wels, Austria', TO_DATE('2016/09/29', 'YYYY/MM/DD'), TO_DATE('2016/10/01', 'YYYY/MM/DD'), 'Retter-Trade Fair for Security, Fire Fighting, Civil Defence, Rescue and Emergency Services' from dual
union all select 55, 3, 'Sao Paulo, Brazil', TO_DATE('2016/10/05', 'YYYY/MM/DD'), TO_DATE('2016/10/07', 'YYYY/MM/DD'), 'FISP-International Safety and Protection Fair' from dual
union all select 56, 3, 'Dresden, Germany', TO_DATE('2016/10/06', 'YYYY/MM/DD'), TO_DATE('2016/10/08', 'YYYY/MM/DD'), 'FLORIAN mit aescutec-Trade Fair for Fire Brigades, Fire Protection and Disaster Control' from dual
union all select 57, 3, 'Hamburg, Germany', TO_DATE('2016/10/11', 'YYYY/MM/DD'), TO_DATE('2016/10/13', 'YYYY/MM/DD'), 'Arbeitsschutz Aktuell-Safety and Health Conference and Trade Fair' from dual
union all select 58, 3, 'Kiev, Ukraine', TO_DATE('2016/10/11', 'YYYY/MM/DD'), TO_DATE('2016/10/14', 'YYYY/MM/DD'), 'PROTECTION TECHNOLOGIES-International Trade Fair' from dual
union all select 59, 3, 'Kiev, Ukraine', TO_DATE('2016/10/11', 'YYYY/MM/DD'), TO_DATE('2016/10/14', 'YYYY/MM/DD'), 'FIRE TECH-Trade Fair' from dual
union all select 60, 3, 'Anaheim, USA', TO_DATE('2016/10/17', 'YYYY/MM/DD'), TO_DATE('2016/10/19', 'YYYY/MM/DD'), 'NSC Congress & Expo-National Safety Council Congress and Expo' from dual
union all select 61, 3, 'Nuremberg, Germany', TO_DATE('2016/10/18', 'YYYY/MM/DD'), TO_DATE('2016/10/20', 'YYYY/MM/DD'), 'it-sa-The IT Security Expo and Congress' from dual
union all select 62, 3, 'Kiev, Ukraine', TO_DATE('2016/10/18', 'YYYY/MM/DD'), TO_DATE('2016/10/21', 'YYYY/MM/DD'), 'BEZPEKA / SECURITY-International Trade Show of Security Industry - Powered by IFSEC International' from dual
union all select 63, 3, 'Port Elizabeth, South Africa', TO_DATE('2016/10/19', 'YYYY/MM/DD'), TO_DATE('2016/10/20', 'YYYY/MM/DD'), 'SCITEC-Security, Construction and Industrial Technology Exhibition and Conference' from dual
union all select 64, 3, 'Bologna, Italy', TO_DATE('2016/10/19', 'YYYY/MM/DD'), TO_DATE('2016/10/21', 'YYYY/MM/DD'), 'AMBIENTE LAVORO-International Exhibition of Health and Safety at Working Places' from dual
union all select 65, 3, 'Tokyo, Japan', TO_DATE('2016/10/19', 'YYYY/MM/DD'), TO_DATE('2016/10/21', 'YYYY/MM/DD'), 'RISCON TOKYO-Security and Safety Trade Expo' from dual
union all select 66, 3, 'Stockholm, Sweden', TO_DATE('2016/10/25', 'YYYY/MM/DD'), TO_DATE('2016/10/27', 'YYYY/MM/DD'), 'SKYDD-Protection and Security Expo' from dual
union all select 67, 3, 'Bangkok, Thailand', TO_DATE('2016/11/01', 'YYYY/MM/DD'), TO_DATE('2016/11/01', 'YYYY/MM/DD'), 'Secutech Thailand-International Trade Fair for Security, Fire and Safety' from dual
union all select 68, 3, 'Lausanne, Switzerland', TO_DATE('2016/11/02', 'YYYY/MM/DD'), TO_DATE('2016/11/04', 'YYYY/MM/DD'), 'SECURITE Lausanne-Regional Trade Fair for Safety and Security' from dual
union all select 69, 3, 'Paris, France', TO_DATE('2016/11/07', 'YYYY/MM/DD'), TO_DATE('2016/11/09', 'YYYY/MM/DD'), 'EXPOPROTECTION-International Security, Risk Prevention and Fire Fighting Exhibition' from dual
union all select 70, 3, 'St. Petersburg, Russia', TO_DATE('2016/11/08', 'YYYY/MM/DD'), TO_DATE('2016/11/10', 'YYYY/MM/DD'), 'Sfitex / Securika-International Exhibition of Security and Fire Protection Equipment and Products' from dual
union all select 71, 3, 'Nairobi, Kenya', TO_DATE('2016/11/08', 'YYYY/MM/DD'), TO_DATE('2016/11/10', 'YYYY/MM/DD'), 'Securexpo East Africa-Commercial, Homeland Security, Fire and Safety Exhibition for the East African Market' from dual
union all select 72, 3, 'Riyadh, Saudi Arabia', TO_DATE('2016/11/14', 'YYYY/MM/DD'), TO_DATE('2016/11/16', 'YYYY/MM/DD'), 'IFSEC ARABIA-The International Event for Industrial and Commercial Security, Fire and Civil Defence' from dual
union all select 73, 3, 'New York, USA', TO_DATE('2016/11/16', 'YYYY/MM/DD'), TO_DATE('2016/11/17', 'YYYY/MM/DD'), 'ISC East-Security Solutions and the Guidance to Apply Them' from dual
union all select 74, 3, 'Cairo, Egypt', TO_DATE('2016/12/04', 'YYYY/MM/DD'), TO_DATE('2016/12/04', 'YYYY/MM/DD'), 'MEFSEC-Middle East Fire, Safety and Security Exhibition' from dual
union all select 75, 3, 'New Delhi, India', TO_DATE('2016/12/08', 'YYYY/MM/DD'), TO_DATE('2016/12/10', 'YYYY/MM/DD'), 'IFSEC India-International Security Event' from dual
union all select 76, 3, 'Isfahan, Iran', TO_DATE('2016/12/13', 'YYYY/MM/DD'), TO_DATE('2016/12/16', 'YYYY/MM/DD'), 'Exhibition for Safety, Security and Modern Fire Protection Technologies' from dual
union all select 77, 3, 'Brussels, Belgium', TO_DATE('2017/03/29', 'YYYY/MM/DD'), TO_DATE('2017/03/30', 'YYYY/MM/DD'), 'SECURA-Exhibition on Health and Well-being, Security and Safety at Work' from dual
union all select 78, 3, 'Katowice, Poland', TO_DATE('2017/04/25', 'YYYY/MM/DD'), TO_DATE('2017/04/27', 'YYYY/MM/DD'), 'BHP-Industrial Safety and Fire Protection Fair' from dual
union all select 79, 3, 'Frankfurt/Main, Germany', TO_DATE('2017/05/09', 'YYYY/MM/DD'), TO_DATE('2017/05/12', 'YYYY/MM/DD'), 'Techtextil-Leading International Trade Fair for Technical Textiles and Nonwovens' from dual
union all select 80, 3, 'Lisbon, Portugal', TO_DATE('2017/05/17', 'YYYY/MM/DD'), TO_DATE('2017/05/17', 'YYYY/MM/DD'), 'SEGUREX-International Security and Safety Exhibition' from dual
union all select 81, 3, 'Trencin, Slovak Republic', TO_DATE('2017/05/17', 'YYYY/MM/DD'), TO_DATE('2017/05/17', 'YYYY/MM/DD'), 'FIRECO-International Exhibition for Fire Protection and Security Systems' from dual
union all select 82, 3, 'Brno, Czech Republic', TO_DATE('2017/05/31', 'YYYY/MM/DD'), TO_DATE('2017/06/02', 'YYYY/MM/DD'), 'PYROS / ISET-International Fair of Fire Fighting Equipment, Security Technology and Services' from dual
union all select 83, 3, 'Belo Horizonte, Brazil', TO_DATE('2017/08/16', 'YYYY/MM/DD'), TO_DATE('2017/08/18', 'YYYY/MM/DD'), 'BRASEG-Security and Safety Exhibition' from dual
union all select 84, 3, 'Sydney, Australia', TO_DATE('2017/09/04', 'YYYY/MM/DD'), TO_DATE('2017/09/07', 'YYYY/MM/DD'), 'AFAC powered by INTERSCHUTZ-Fire Fighting, Emergency Services and Public Safety Conference and Trade Exhibition' from dual
union all select 85, 3, 'Paris, France', TO_DATE('2017/09/17', 'YYYY/MM/DD'), TO_DATE('2017/09/17', 'YYYY/MM/DD'), 'APS - Alarmes Protection Sécurité-Specialist Exhibition of Electronic and Physical Security, Industrial Security - Theft, Intrusion and Fire' from dual
union all select 86, 3, 'Buenos Aires, Argentina', TO_DATE('2017/09/17', 'YYYY/MM/DD'), TO_DATE('2017/09/17', 'YYYY/MM/DD'), 'Seguriexpo Buenos Aires-South American Trade Fair for Commercial and Information Security' from dual
union all select 87, 3, 'Berlin, Germany', TO_DATE('2017/09/19', 'YYYY/MM/DD'), TO_DATE('2017/09/22', 'YYYY/MM/DD'), 'CMS-CMS - Cleaning.Management.Services. - International Trade Fair and Congress' from dual
union all select 88, 3, 'Indianapolis, USA', TO_DATE('2017/09/22', 'YYYY/MM/DD'), TO_DATE('2017/09/27', 'YYYY/MM/DD'), 'NSC Congress & Expo-National Safety Council Congress and Expo' from dual
union all select 89, 3, 'Amsterdam, Netherlands', TO_DATE('2017/09/26', 'YYYY/MM/DD'), TO_DATE('2017/09/28', 'YYYY/MM/DD'), 'SSA - Safety & Security Amsterdam-Safety, Security and Fire Safety Trade Fair' from dual
union all select 90, 3, 'Shenzhen, China, PR', TO_DATE('2017/10/17', 'YYYY/MM/DD'), TO_DATE('2017/10/17', 'YYYY/MM/DD'), 'CPSE-China Public Security Expo' from dual
union all select 91, 3, 'Dusseldorf, Germany', TO_DATE('2017/10/17', 'YYYY/MM/DD'), TO_DATE('2017/10/20', 'YYYY/MM/DD'), 'A+A-Safety, Security and Health at Work - International Trade Fair with Congress' from dual
union all select 92, 3, 'Zurich, Switzerland', TO_DATE('2017/11/14', 'YYYY/MM/DD'), TO_DATE('2017/11/17', 'YYYY/MM/DD'), 'SICHERHEIT-Exhibition for Safety and Security' from dual
union all select 93, 3, 'Milan, Italy', TO_DATE('2017/11/15', 'YYYY/MM/DD'), TO_DATE('2017/11/17', 'YYYY/MM/DD'), 'SICUREZZA-International Security Exhibition' from dual
union all select 94, 3, 'Helsinki, Finland', TO_DATE('2017/11/21', 'YYYY/MM/DD'), TO_DATE('2017/11/23', 'YYYY/MM/DD'), 'FinnSec-Helsinki International Safety and Security Fair' from dual
union all select 95, 3, 'Hanover, Germany', TO_DATE('2020/06/15', 'YYYY/MM/DD'), TO_DATE('2020/06/20', 'YYYY/MM/DD'), 'INTERSCHUTZ-International Exhibition for Fire Prevention, Disaster Relief, Rescue and Safety & Security' from dual
