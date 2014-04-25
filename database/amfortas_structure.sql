CREATE TABLE operating_system_setting (
  operating_system_setting_id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  name_key VARCHAR(255) NULL,
  name_default VARCHAR(255) NULL,
  PRIMARY KEY(operating_system_setting_id)
);

CREATE TABLE role (
  role_id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  name_key VARCHAR(255) NULL,
  name_default VARCHAR(255) NULL,
  PRIMARY KEY(role_id)
);

CREATE TABLE language_experience (
  language_experience_id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  name_key VARCHAR(255) NULL,
  name_default VARCHAR(255) NULL,
  PRIMARY KEY(language_experience_id)
);

CREATE TABLE operating_system (
  operationg_system_id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  name_key VARCHAR(255) NULL,
  name_default VARCHAR(255) NULL,
  vendor VARCHAR(255) NULL,
  PRIMARY KEY(operationg_system_id)
);

CREATE TABLE sex (
  sex_id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  name_key VARCHAR(255) NULL,
  name_default VARCHAR(255) NULL,
  PRIMARY KEY(sex_id)
);

CREATE TABLE user_agent_setting (
  user_agent_setting_id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  name_key VARCHAR(255) NULL,
  name_default VARCHAR(255) NULL,
  PRIMARY KEY(user_agent_setting_id)
);

CREATE TABLE user_agent_types (
  user_agent_types_id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  name_key VARCHAR(255) NULL,
  name_default VARCHAR(255) NULL,
  PRIMARY KEY(user_agent_types_id)
);

CREATE TABLE test_suite (
  test_suite_id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  title VARCHAR(255) NULL,
  description VARCHAR(255) NULL,
  index_file_uri VARCHAR(255) NULL,
  metadata_files_uri VARCHAR(255) NULL,
  test_files_uri VARCHAR(255) NULL,
  mapping_file_uri VARCHAR(255) NULL,
  timesPerTestCase INTEGER UNSIGNED NULL,
  testCasesPerUser INTEGER UNSIGNED NULL,
  date_start DATETIME NULL,
  date_end DATETIME NULL,
  is_active BOOL NULL,
  object BLOB NULL,
  PRIMARY KEY(test_suite_id)
);

CREATE TABLE user_agent_plugin (
  user_agent_plugin_id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  name_key VARCHAR(255) NULL,
  name_default VARCHAR(255) NULL,
  vendor VARCHAR(255) NULL,
  PRIMARY KEY(user_agent_plugin_id)
);

CREATE TABLE disability (
  disability_id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  name_key VARCHAR(255) NULL,
  name_default VARCHAR(255) NULL,
  PRIMARY KEY(disability_id)
);

CREATE TABLE hours_per_week (
  hours_per_week_id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  name_key VARCHAR(255) NULL,
  name_default VARCHAR(255) NULL,
  PRIMARY KEY(hours_per_week_id)
);

CREATE TABLE at_experience (
  at_experience_id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  name_key VARCHAR(255) NULL,
  name_default VARCHAR(255) NULL,
  PRIMARY KEY(at_experience_id)
);

CREATE TABLE device (
  device_id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  name_key VARCHAR(255) NULL,
  name_default VARCHAR(255) NULL,
  PRIMARY KEY(device_id)
);

CREATE TABLE age_range (
  age_range_id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  name_key VARCHAR(255) NULL,
  name_default VARCHAR(255) NULL,
  PRIMARY KEY(age_range_id)
);

CREATE TABLE language (
  language_id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  name_key Varchar(255) NULL,
  name_default VARCHAR(255) NULL,
  shortcode VARCHAR(5) NULL,
  PRIMARY KEY(language_id)
);

CREATE TABLE assistive_technology_types (
  assistive_technology_types_id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  name_key VARCHAR(255) NULL,
  name_default VARCHAR(255) NULL,
  PRIMARY KEY(assistive_technology_types_id)
);

CREATE TABLE user_uses_os (
  user_uses_os_id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  operationg_system_id INTEGER UNSIGNED NOT NULL,
  version VARCHAR(255) NULL,
  os_other VARCHAR(255) NULL,
  PRIMARY KEY(user_uses_os_id),
  INDEX user_uses_os_FKIndex1(operationg_system_id),
  FOREIGN KEY(operationg_system_id)
    REFERENCES operating_system(operationg_system_id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION
);

CREATE TABLE assistive_technology (
  assistive_technology_id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  assistive_technology_types_id INTEGER UNSIGNED NOT NULL,
  name_key VARCHAR(255) NULL,
  name_default VARCHAR(255) NULL,
  vendor VARCHAR(255) NULL,
  visible BOOL NULL,
  PRIMARY KEY(assistive_technology_id),
  INDEX assistive_technology_FKIndex1(assistive_technology_types_id),
  FOREIGN KEY(assistive_technology_types_id)
    REFERENCES assistive_technology_types(assistive_technology_types_id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION
);

CREATE TABLE user_agent (
  user_agent_id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  user_agent_types_id INTEGER UNSIGNED NOT NULL,
  name_key VARCHAR(255) NULL,
  name_default VARCHAR(255) NULL,
  vendor VARCHAR(255) NULL,
  visible BOOL NULL,
  PRIMARY KEY(user_agent_id),
  INDEX user_agent_FKIndex1(user_agent_types_id),
  FOREIGN KEY(user_agent_types_id)
    REFERENCES user_agent_types(user_agent_types_id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION
);

CREATE TABLE user_uses_device (
  user_uses_device_id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  at_experience_id INTEGER UNSIGNED NOT NULL,
  device_id INTEGER UNSIGNED NOT NULL,
  device_other VARCHAR(255) NULL,
  PRIMARY KEY(user_uses_device_id),
  INDEX user_uses_device_FKIndex1(device_id),
  INDEX user_uses_device_FKIndex2(at_experience_id),
  FOREIGN KEY(device_id)
    REFERENCES device(device_id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION,
  FOREIGN KEY(at_experience_id)
    REFERENCES at_experience(at_experience_id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION
);

CREATE TABLE user_uses_os_setting (
  user_uses_os_id INTEGER UNSIGNED NOT NULL,
  operating_system_setting_id INTEGER UNSIGNED NOT NULL,
  setting_other VARCHAR(255) NULL,
  PRIMARY KEY(user_uses_os_id, operating_system_setting_id),
  INDEX user_uses_os_has_operating_system_setting_FKIndex1(user_uses_os_id),
  INDEX user_uses_os_has_operating_system_setting_FKIndex2(operating_system_setting_id),
  FOREIGN KEY(user_uses_os_id)
    REFERENCES user_uses_os(user_uses_os_id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION,
  FOREIGN KEY(operating_system_setting_id)
    REFERENCES operating_system_setting(operating_system_setting_id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION
);

CREATE TABLE user (
  user_id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  sex_id INTEGER UNSIGNED NOT NULL,
  language_id INTEGER UNSIGNED NOT NULL,
  password Varchar(255) NULL,
  name_first Varchar(255) NULL,
  name_last Varchar(255) NULL,
  email Varchar(255) NULL,
  language_native_other Varchar(255) NULL,
  date_register DATETIME NULL,
  status_user TINYINT UNSIGNED NULL,
  mail_conf_hash VARCHAR(255) NULL,
  PRIMARY KEY(user_id),
  INDEX user_FKIndex1(language_id),
  INDEX user_FKIndex2(sex_id),
  UNIQUE INDEX user_mail(email),
  FOREIGN KEY(language_id)
    REFERENCES language(language_id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION,
  FOREIGN KEY(sex_id)
    REFERENCES sex(sex_id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION
);

CREATE TABLE test_profile (
  test_profile_id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  user_uses_device_id INTEGER UNSIGNED NOT NULL,
  user_uses_os_id INTEGER UNSIGNED NOT NULL,
  user_id INTEGER UNSIGNED NOT NULL,
  is_active BOOL NULL,
  date_creation DATETIME NULL,
  last_login DATETIME NULL,
  PRIMARY KEY(test_profile_id),
  INDEX profile_FKIndex1(user_id),
  INDEX profile_FKIndex3(user_uses_os_id),
  INDEX profile_FKIndex2(user_uses_device_id),
  FOREIGN KEY(user_id)
    REFERENCES user(user_id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION,
  FOREIGN KEY(user_uses_os_id)
    REFERENCES user_uses_os(user_uses_os_id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION,
  FOREIGN KEY(user_uses_device_id)
    REFERENCES user_uses_device(user_uses_device_id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION
);

CREATE TABLE user_uses_assistive_technology (
  user_uses_at_id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  test_profile_id INTEGER UNSIGNED NOT NULL,
  assistive_technology_id INTEGER UNSIGNED NOT NULL,
  at_experience_id INTEGER UNSIGNED NOT NULL,
  version VARCHAR(255) NULL,
  assistive_technology_other VARCHAR(255) NULL,
  PRIMARY KEY(user_uses_at_id),
  INDEX user_uses_assistive_technology_FKIndex1(at_experience_id),
  INDEX user_uses_assistive_technology_FKIndex2(assistive_technology_id),
  INDEX user_uses_assistive_technology_FKIndex3(test_profile_id),
  FOREIGN KEY(at_experience_id)
    REFERENCES at_experience(at_experience_id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION,
  FOREIGN KEY(assistive_technology_id)
    REFERENCES assistive_technology(assistive_technology_id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION,
  FOREIGN KEY(test_profile_id)
    REFERENCES test_profile(test_profile_id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION
);

CREATE TABLE user_profile (
  user_profile_id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  language_experience_id INTEGER UNSIGNED NOT NULL,
  age_range_id INTEGER UNSIGNED NOT NULL,
  hours_per_week_id INTEGER UNSIGNED NOT NULL,
  disability_other VARCHAR(255) NULL,
  is_active BOOL NULL,
  date_creation DATETIME NULL,
  PRIMARY KEY(user_profile_id),
  INDEX user_profile_FKIndex1(hours_per_week_id),
  INDEX user_profile_FKIndex2(age_range_id),
  INDEX user_profile_FKIndex3(language_experience_id),
  FOREIGN KEY(hours_per_week_id)
    REFERENCES hours_per_week(hours_per_week_id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION,
  FOREIGN KEY(age_range_id)
    REFERENCES age_range(age_range_id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION,
  FOREIGN KEY(language_experience_id)
    REFERENCES language_experience(language_experience_id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION
);

CREATE TABLE user_profile_has_disability (
  user_profile_id INTEGER UNSIGNED NOT NULL,
  disability_id INTEGER UNSIGNED NOT NULL,
  PRIMARY KEY(user_profile_id, disability_id),
  INDEX user_profile_has_disability_FKIndex1(user_profile_id),
  INDEX user_profile_has_disability_FKIndex2(disability_id),
  FOREIGN KEY(user_profile_id)
    REFERENCES user_profile(user_profile_id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION,
  FOREIGN KEY(disability_id)
    REFERENCES disability(disability_id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION
);

CREATE TABLE test_profile_has_user_profile (
  test_profile_id INTEGER UNSIGNED NOT NULL,
  user_profile_id INTEGER UNSIGNED NOT NULL,
  PRIMARY KEY(test_profile_id, user_profile_id),
  INDEX test_profile_has_user_profile_FKIndex1(test_profile_id),
  INDEX test_profile_has_user_profile_FKIndex2(user_profile_id),
  FOREIGN KEY(test_profile_id)
    REFERENCES test_profile(test_profile_id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION,
  FOREIGN KEY(user_profile_id)
    REFERENCES user_profile(user_profile_id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION
);

CREATE TABLE test_profile_has_test_suite (
  test_profile_id INTEGER UNSIGNED NOT NULL,
  test_suite_id INTEGER UNSIGNED NOT NULL,
  PRIMARY KEY(test_profile_id, test_suite_id),
  INDEX test_profile_has_test_suite_FKIndex1(test_profile_id),
  INDEX test_profile_has_test_suite_FKIndex2(test_suite_id),
  FOREIGN KEY(test_profile_id)
    REFERENCES test_profile(test_profile_id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION,
  FOREIGN KEY(test_suite_id)
    REFERENCES test_suite(test_suite_id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION
);

CREATE TABLE testcase_set (
  testcase_set_id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  test_suite_id INTEGER UNSIGNED NOT NULL,
  test_profile_id INTEGER UNSIGNED NOT NULL,
  date_creation DATETIME NULL,
  done BOOL NULL,
  PRIMARY KEY(testcase_set_id),
  INDEX testcase_set_FKIndex1(test_profile_id),
  INDEX testcase_set_FKIndex2(test_suite_id),
  FOREIGN KEY(test_profile_id)
    REFERENCES test_profile(test_profile_id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION,
  FOREIGN KEY(test_suite_id)
    REFERENCES test_suite(test_suite_id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION
);

CREATE TABLE user_has_role (
  user_id INTEGER UNSIGNED NOT NULL,
  role_id INTEGER UNSIGNED NOT NULL,
  PRIMARY KEY(user_id, role_id),
  INDEX user_has_role_FKIndex1(user_id),
  INDEX user_has_role_FKIndex2(role_id),
  FOREIGN KEY(user_id)
    REFERENCES user(user_id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION,
  FOREIGN KEY(role_id)
    REFERENCES role(role_id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION
);

CREATE TABLE user_uses_user_agent (
  user_uses_ua_id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  user_agent_id INTEGER UNSIGNED NOT NULL,
  test_profile_id INTEGER UNSIGNED NOT NULL,
  at_experience_id INTEGER UNSIGNED NOT NULL,
  version VARCHAR(255) NULL,
  browser_other VARCHAR(255) NULL,
  PRIMARY KEY(user_uses_ua_id),
  INDEX user_uses_user_agent_FKIndex1(at_experience_id),
  INDEX user_uses_user_agent_FKIndex2(test_profile_id),
  INDEX user_uses_user_agent_FKIndex3(user_agent_id),
  FOREIGN KEY(at_experience_id)
    REFERENCES at_experience(at_experience_id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION,
  FOREIGN KEY(test_profile_id)
    REFERENCES test_profile(test_profile_id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION,
  FOREIGN KEY(user_agent_id)
    REFERENCES user_agent(user_agent_id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION
);

CREATE TABLE testresult (
  testresult_id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  test_suite_id INTEGER UNSIGNED NOT NULL,
  user_profile_id INTEGER UNSIGNED NOT NULL,
  test_profile_id INTEGER UNSIGNED NOT NULL,
  scenario_id VARCHAR(255) NULL,
  testcase_id VARCHAR(255) NULL,
  date DATETIME NULL,
  PRIMARY KEY(testresult_id),
  INDEX testresult_FKIndex1(test_profile_id),
  INDEX testresult_FKIndex2(user_profile_id),
  INDEX testresult_FKIndex3(test_suite_id),
  FOREIGN KEY(test_profile_id)
    REFERENCES test_profile(test_profile_id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION,
  FOREIGN KEY(user_profile_id)
    REFERENCES user_profile(user_profile_id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION,
  FOREIGN KEY(test_suite_id)
    REFERENCES test_suite(test_suite_id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION
);

CREATE TABLE yesnoopenquestion (
  yesnoopen_id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  testresult_id INTEGER UNSIGNED NOT NULL,
  choice INTEGER NULL,
  text TEXT NULL,
  PRIMARY KEY(yesnoopen_id),
  INDEX yesnoopenquestion_FKIndex1(testresult_id),
  FOREIGN KEY(testresult_id)
    REFERENCES testresult(testresult_id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION
);

CREATE TABLE openquestion (
  open_id INTEGER NOT NULL AUTO_INCREMENT,
  testresult_id INTEGER UNSIGNED NOT NULL,
  text TEXT NULL,
  PRIMARY KEY(open_id),
  INDEX openquestion_FKIndex1(testresult_id),
  FOREIGN KEY(testresult_id)
    REFERENCES testresult(testresult_id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION
);

CREATE TABLE header_log (
  header_log_id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  testresult_id INTEGER UNSIGNED NOT NULL,
  header_key VARCHAR(255) NULL,
  header_value VARCHAR(255) NULL,
  PRIMARY KEY(header_log_id),
  INDEX header_log_FKIndex1(testresult_id),
  FOREIGN KEY(testresult_id)
    REFERENCES testresult(testresult_id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION
);

CREATE TABLE likertscale (
  likert_id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  testresult_id INTEGER UNSIGNED NOT NULL,
  level INTEGER NULL,
  PRIMARY KEY(likert_id),
  INDEX likertscale_FKIndex1(testresult_id),
  FOREIGN KEY(testresult_id)
    REFERENCES testresult(testresult_id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION
);

CREATE TABLE multiplechoice (
  multiple_id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  testresult_id INTEGER UNSIGNED NOT NULL,
  level INTEGER NULL,
  PRIMARY KEY(multiple_id),
  INDEX multiplechoice_FKIndex1(testresult_id),
  FOREIGN KEY(testresult_id)
    REFERENCES testresult(testresult_id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION
);

CREATE TABLE yesnoquestion (
  yesno_id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  testresult_id INTEGER UNSIGNED NOT NULL,
  choice INTEGER NULL,
  PRIMARY KEY(yesno_id),
  INDEX yesnoquestion_FKIndex1(testresult_id),
  FOREIGN KEY(testresult_id)
    REFERENCES testresult(testresult_id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION
);

CREATE TABLE testcase_pool (
  testcase_pool_id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  testcase_set_id INTEGER UNSIGNED NOT NULL,
  testcase_id VARCHAR(255) NULL,
  scenario_id VARCHAR(255) NULL,
  date_start DATETIME NULL,
  date_finished DATETIME NULL,
  finished BOOL NULL,
  PRIMARY KEY(testcase_pool_id),
  INDEX testcase_set_detail_FKIndex1(testcase_set_id),
  FOREIGN KEY(testcase_set_id)
    REFERENCES testcase_set(testcase_set_id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION
);

CREATE TABLE user_uses_ua_setting (
  user_agent_setting_id INTEGER UNSIGNED NOT NULL,
  user_uses_ua_id INTEGER UNSIGNED NOT NULL,
  setting_other VARCHAR(255) NULL,
  PRIMARY KEY(user_agent_setting_id, user_uses_ua_id),
  INDEX user_uses_user_agent_has_user_agent_setting_FKIndex1(user_uses_ua_id),
  INDEX user_uses_user_agent_has_user_agent_setting_FKIndex2(user_agent_setting_id),
  FOREIGN KEY(user_uses_ua_id)
    REFERENCES user_uses_user_agent(user_uses_ua_id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION,
  FOREIGN KEY(user_agent_setting_id)
    REFERENCES user_agent_setting(user_agent_setting_id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION
);

CREATE TABLE user_uses_ua_plugin (
  user_agent_plugin_id INTEGER UNSIGNED NOT NULL,
  user_uses_ua_id INTEGER UNSIGNED NOT NULL,
  version VARCHAR(255) NULL,
  PRIMARY KEY(user_agent_plugin_id, user_uses_ua_id),
  INDEX user_uses_user_agent_has_user_agent_plugin_FKIndex1(user_uses_ua_id),
  INDEX user_uses_user_agent_has_user_agent_plugin_FKIndex2(user_agent_plugin_id),
  FOREIGN KEY(user_uses_ua_id)
    REFERENCES user_uses_user_agent(user_uses_ua_id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION,
  FOREIGN KEY(user_agent_plugin_id)
    REFERENCES user_agent_plugin(user_agent_plugin_id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION
);


CREATE TABLE  `amfortas`.`tests_exclude` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `testCaseId` varchar(100) NOT NULL,
  `scenarioId` varchar(100) NOT NULL default '*',
  PRIMARY KEY  (`id`)
);