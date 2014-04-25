-- 
-- Dumping data for table `age_range`
-- 

INSERT INTO `age_range` (`age_range_id`, `name_key`, `name_default`) VALUES (1, 'amformas.recruit.ans.age_range_id.0', NULL);
INSERT INTO `age_range` (`age_range_id`, `name_key`, `name_default`) VALUES (2, 'amformas.recruit.ans.age_range_id.1', 'Between 18 and 20');
INSERT INTO `age_range` (`age_range_id`, `name_key`, `name_default`) VALUES (3, 'amformas.recruit.ans.age_range_id.2', 'Between 21 and 30');
INSERT INTO `age_range` (`age_range_id`, `name_key`, `name_default`) VALUES (4, 'amformas.recruit.ans.age_range_id.3', 'Between 31 and 40');
INSERT INTO `age_range` (`age_range_id`, `name_key`, `name_default`) VALUES (5, 'amformas.recruit.ans.age_range_id.4', 'Between 41 and 50');
INSERT INTO `age_range` (`age_range_id`, `name_key`, `name_default`) VALUES (6, 'amformas.recruit.ans.age_range_id.5', 'Between 51 and 60');
INSERT INTO `age_range` (`age_range_id`, `name_key`, `name_default`) VALUES (7, 'amformas.recruit.ans.age_range_id.6', 'Between 61 and 70');
INSERT INTO `age_range` (`age_range_id`, `name_key`, `name_default`) VALUES (8, 'amformas.recruit.ans.age_range_id.7', 'Over 70');

-- 
-- Dumping data for table `assistive_technology_types`
-- 

INSERT INTO `assistive_technology_types` (`assistive_technology_types_id`, `name_key`, `name_default`) VALUES (1, 'amformas.recruit.ans.assistive_technology_types_id.0', NULL);
INSERT INTO `assistive_technology_types` (`assistive_technology_types_id`, `name_key`, `name_default`) VALUES (2, 'amformas.recruit.ans.assistive_technology_types_id.1', 'screenreader');
INSERT INTO `assistive_technology_types` (`assistive_technology_types_id`, `name_key`, `name_default`) VALUES (3, 'amformas.recruit.ans.assistive_technology_types_id.3', 'screenreader with magnification');
INSERT INTO `assistive_technology_types` (`assistive_technology_types_id`, `name_key`, `name_default`) VALUES (4, 'amformas.recruit.ans.assistive_technology_types_id.4', 'magnification software');
INSERT INTO `assistive_technology_types` (`assistive_technology_types_id`, `name_key`, `name_default`) VALUES (5, 'amformas.recruit.ans.assistive_technology_types_id.5', 'speech and hearing support software');
INSERT INTO `assistive_technology_types` (`assistive_technology_types_id`, `name_key`, `name_default`) VALUES (6, 'amformas.recruit.ans.assistive_technology_types_id.6', 'speech recognition software');
INSERT INTO `assistive_technology_types` (`assistive_technology_types_id`, `name_key`, `name_default`) VALUES (7, 'amformas.recruit.ans.assistive_technology_types_id.7', 'Braille display');
INSERT INTO `assistive_technology_types` (`assistive_technology_types_id`, `name_key`, `name_default`) VALUES (8, 'amformas.recruit.ans.assistive_technology_types_id.8', 'alternative input devices');

-- --------------------------------------------------------

-- 
-- Dumping data for table `at_experience`
-- 

INSERT INTO `at_experience` (`at_experience_id`, `name_key`, `name_default`) VALUES (1, 'amformas.recruit.ans.common.experience_id.0', NULL);
INSERT INTO `at_experience` (`at_experience_id`, `name_key`, `name_default`) VALUES (2, 'amformas.recruit.ans.common.experience_id.1', 'Not at all experienced');
INSERT INTO `at_experience` (`at_experience_id`, `name_key`, `name_default`) VALUES (3, 'amformas.recruit.ans.common.experience_id.2', 'Not very experienced');
INSERT INTO `at_experience` (`at_experience_id`, `name_key`, `name_default`) VALUES (4, 'amformas.recruit.ans.common.experience_id.3', 'Experienced');
INSERT INTO `at_experience` (`at_experience_id`, `name_key`, `name_default`) VALUES (5, 'amformas.recruit.ans.common.experience_id.4', 'Quite experienced');
INSERT INTO `at_experience` (`at_experience_id`, `name_key`, `name_default`) VALUES (6, 'amformas.recruit.ans.common.experience_id.5', 'Very experienced');

-- 
-- Dumping data for table `device`
-- 

INSERT INTO `device` (`device_id`, `name_key`, `name_default`) VALUES (1, 'amformas.recruit.ans.common.other', NULL);
INSERT INTO `device` (`device_id`, `name_key`, `name_default`) VALUES (2, 'amformas.recruit.ans.device_id.1', 'PC');
INSERT INTO `device` (`device_id`, `name_key`, `name_default`) VALUES (3, 'amformas.recruit.ans.device_id.2', 'Television');
INSERT INTO `device` (`device_id`, `name_key`, `name_default`) VALUES (4, 'amformas.recruit.ans.device_id.3', 'PDA');
INSERT INTO `device` (`device_id`, `name_key`, `name_default`) VALUES (5, 'amformas.recruit.ans.device_id.4', 'MobilePhone');

-- 
-- Dumping data for table `disability`
-- 

INSERT INTO `disability` (`disability_id`, `name_key`, `name_default`) VALUES (1, 'amformas.recruit.ans.disability_id.0', NULL);
INSERT INTO `disability` (`disability_id`, `name_key`, `name_default`) VALUES (2, 'amformas.recruit.ans.disability_id.1', 'blindness');
INSERT INTO `disability` (`disability_id`, `name_key`, `name_default`) VALUES (3, 'amformas.recruit.ans.disability_id.2', 'colour vision deficiency');
INSERT INTO `disability` (`disability_id`, `name_key`, `name_default`) VALUES (4, 'amformas.recruit.ans.disability_id.3', 'low vision');
INSERT INTO `disability` (`disability_id`, `name_key`, `name_default`) VALUES (5, 'amformas.recruit.ans.disability_id.4', 'deafness');
INSERT INTO `disability` (`disability_id`, `name_key`, `name_default`) VALUES (6, 'amformas.recruit.ans.disability_id.5', 'hard of hearing');
INSERT INTO `disability` (`disability_id`, `name_key`, `name_default`) VALUES (7, 'amformas.recruit.ans.disability_id.6', 'dyslexia');
INSERT INTO `disability` (`disability_id`, `name_key`, `name_default`) VALUES (8, 'amformas.recruit.ans.disability_id.7', 'intellectual disability');
INSERT INTO `disability` (`disability_id`, `name_key`, `name_default`) VALUES (9, 'amformas.recruit.ans.disability_id.8', 'dexterity impairment');
INSERT INTO `disability` (`disability_id`, `name_key`, `name_default`) VALUES (10, 'amformas.recruit.ans.disability_id.9', 'motor impairment');
INSERT INTO `disability` (`disability_id`, `name_key`, `name_default`) VALUES (11, 'amformas.recruit.ans.disability_id.10', 'learning disability');
INSERT INTO `disability` (`disability_id`, `name_key`, `name_default`) VALUES (12, 'amformas.recruit.ans.disability_id.11', 'speech impairment');
INSERT INTO `disability` (`disability_id`, `name_key`, `name_default`) VALUES (13, 'amformas.recruit.ans.disability_id.12', 'aphasia');
INSERT INTO `disability` (`disability_id`, `name_key`, `name_default`) VALUES (14, 'amformas.recruit.ans.disability_id.13', 'functional illiteracy');
INSERT INTO `disability` (`disability_id`, `name_key`, `name_default`) VALUES (15, 'amformas.recruit.ans.disability_id.14', 'ADHD');
INSERT INTO `disability` (`disability_id`, `name_key`, `name_default`) VALUES (16, 'amformas.recruit.ans.disability_id.15', 'deaf-blindness');
INSERT INTO `disability` (`disability_id`, `name_key`, `name_default`) VALUES (17, 'amformas.recruit.ans.disability_id.16', 'dyscalculus');

-- 
-- Dumping data for table `hours_per_week`
-- 

INSERT INTO `hours_per_week` (`hours_per_week_id`, `name_key`, `name_default`) VALUES (1, 'amformas.recruit.ans.hours_per_week_id.0', NULL);
INSERT INTO `hours_per_week` (`hours_per_week_id`, `name_key`, `name_default`) VALUES (2, 'amformas.recruit.ans.hours_per_week_id.1', 'Never use the web');
INSERT INTO `hours_per_week` (`hours_per_week_id`, `name_key`, `name_default`) VALUES (3, 'amformas.recruit.ans.hours_per_week_id.2', 'Between 1 and 5 hours per week');
INSERT INTO `hours_per_week` (`hours_per_week_id`, `name_key`, `name_default`) VALUES (4, 'amformas.recruit.ans.hours_per_week_id.3', 'Between 6 and 10 hours per week');
INSERT INTO `hours_per_week` (`hours_per_week_id`, `name_key`, `name_default`) VALUES (5, 'amformas.recruit.ans.hours_per_week_id.4', 'Between 11 and 20 hours per week');
INSERT INTO `hours_per_week` (`hours_per_week_id`, `name_key`, `name_default`) VALUES (6, 'amformas.recruit.ans.hours_per_week_id.5', 'More than 20 hours');

-- --------------------------------------------------------

-- 
-- Dumping data for table `assistive_technology`
-- 

INSERT INTO `assistive_technology` (`assistive_technology_id`, `assistive_technology_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (1, 1, 'amformas.recruit.ans.assistive_technology.null', NULL, NULL, 0);
INSERT INTO `assistive_technology` (`assistive_technology_id`, `assistive_technology_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (2, 1, 'amformas.recruit.ans.common.other', 'Other (please state below)', NULL, 1);
INSERT INTO `assistive_technology` (`assistive_technology_id`, `assistive_technology_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (3, 2, 'amformas.recruit.ans.screen_reader_id.1', 'JAWS', NULL, 1);
INSERT INTO `assistive_technology` (`assistive_technology_id`, `assistive_technology_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (4, 2, 'amformas.recruit.ans.screen_reader_id.2', 'Window-Eyes', NULL, 1);
INSERT INTO `assistive_technology` (`assistive_technology_id`, `assistive_technology_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (5, 2, 'amformas.recruit.ans.screen_reader_id.3', 'HAL', NULL, 1);
INSERT INTO `assistive_technology` (`assistive_technology_id`, `assistive_technology_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (6, 2, 'amformas.recruit.ans.screen_reader_id.4', 'Mobile Speak Pocket', NULL, 0);
INSERT INTO `assistive_technology` (`assistive_technology_id`, `assistive_technology_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (7, 2, 'amformas.recruit.ans.screen_reader_id.5', 'OutSpoken', NULL, 0);
INSERT INTO `assistive_technology` (`assistive_technology_id`, `assistive_technology_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (8, 2, 'amformas.recruit.ans.screen_reader_id.6', 'Reader (Sensory Software)', NULL, 0);
INSERT INTO `assistive_technology` (`assistive_technology_id`, `assistive_technology_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (9, 2, 'amformas.recruit.ans.screen_reader_id.7', 'WinVision', NULL, 0);
INSERT INTO `assistive_technology` (`assistive_technology_id`, `assistive_technology_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (10, 3, 'amformas.recruit.ans.sr_N_magn_sw_id.1', 'ZoomText', NULL, 1);
INSERT INTO `assistive_technology` (`assistive_technology_id`, `assistive_technology_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (11, 3, 'amformas.recruit.ans.sr_N_magn_sw_id.2', 'Supernova', NULL, 1);
INSERT INTO `assistive_technology` (`assistive_technology_id`, `assistive_technology_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (12, 3, 'amformas.recruit.ans.sr_N_magn_sw_id.3', 'VoiceOver', NULL, 1);
INSERT INTO `assistive_technology` (`assistive_technology_id`, `assistive_technology_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (13, 3, 'amformas.recruit.ans.sr_N_magn_sw_id.4', 'Blindows', NULL, 0);
INSERT INTO `assistive_technology` (`assistive_technology_id`, `assistive_technology_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (14, 3, 'amformas.recruit.ans.sr_N_magn_sw_id.5', 'Virgo', NULL, 0);
INSERT INTO `assistive_technology` (`assistive_technology_id`, `assistive_technology_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (15, 3, 'amformas.recruit.ans.sr_N_magn_sw_id.6', 'ZoomText', NULL, 0);
INSERT INTO `assistive_technology` (`assistive_technology_id`, `assistive_technology_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (16, 4, 'amformas.recruit.ans.magn_sw_id.1', 'Lunar', NULL, 1);
INSERT INTO `assistive_technology` (`assistive_technology_id`, `assistive_technology_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (17, 4, 'amformas.recruit.ans.magn_sw_id.2', 'MAGic', NULL, 1);
INSERT INTO `assistive_technology` (`assistive_technology_id`, `assistive_technology_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (18, 4, 'amformas.recruit.ans.magn_sw_id.3', 'Biggy', NULL, 0);
INSERT INTO `assistive_technology` (`assistive_technology_id`, `assistive_technology_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (19, 4, 'amformas.recruit.ans.magn_sw_id.4', 'BigShot', NULL, 0);
INSERT INTO `assistive_technology` (`assistive_technology_id`, `assistive_technology_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (20, 5, 'amformas.recruit.ans.speech_N_read_sw_id.1', 'BrowseAloud', NULL, 1);
INSERT INTO `assistive_technology` (`assistive_technology_id`, `assistive_technology_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (21, 5, 'amformas.recruit.ans.speech_N_read_sw_id.2', 'ReadPlease', NULL, 1);
INSERT INTO `assistive_technology` (`assistive_technology_id`, `assistive_technology_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (22, 5, 'amformas.recruit.ans.speech_N_read_sw_id.3', 'Kurzweil 3000', NULL, 1);
INSERT INTO `assistive_technology` (`assistive_technology_id`, `assistive_technology_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (23, 5, 'amformas.recruit.ans.speech_N_read_sw_id.4', 'e-Read', NULL, 0);
INSERT INTO `assistive_technology` (`assistive_technology_id`, `assistive_technology_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (24, 6, 'amformas.recruit.ans.speechrec_sw_id.1', 'Dragon Naturally Speaking', NULL, 1);
INSERT INTO `assistive_technology` (`assistive_technology_id`, `assistive_technology_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (25, 6, 'amformas.recruit.ans.speechrec_sw_id.2', 'ViaVoice', NULL, 1);
INSERT INTO `assistive_technology` (`assistive_technology_id`, `assistive_technology_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (26, 6, 'amformas.recruit.ans.speechrec_sw_id.3', 'QPointer', NULL, 1);
INSERT INTO `assistive_technology` (`assistive_technology_id`, `assistive_technology_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (27, 6, 'amformas.recruit.ans.speechrec_sw_id.4', 'JawBone', NULL, 0);
INSERT INTO `assistive_technology` (`assistive_technology_id`, `assistive_technology_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (28, 7, 'amformas.recruit.ans.sr_N_braille_id.1', 'Alva Braille display', NULL, 1);
INSERT INTO `assistive_technology` (`assistive_technology_id`, `assistive_technology_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (29, 7, 'amformas.recruit.ans.sr_N_braille_id.2', 'Baum Braille display', NULL, 1);
INSERT INTO `assistive_technology` (`assistive_technology_id`, `assistive_technology_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (30, 7, 'amformas.recruit.ans.sr_N_braille_id.3', 'Papenmeier Braille display', NULL, 1);
INSERT INTO `assistive_technology` (`assistive_technology_id`, `assistive_technology_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (31, 7, 'amformas.recruit.ans.sr_N_braille_id.4', 'Tieman or Optelec Braille display', NULL, 1);
INSERT INTO `assistive_technology` (`assistive_technology_id`, `assistive_technology_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (32, 7, 'amformas.recruit.ans.sr_N_braille_id.5', 'Braille display (unspecified product)', NULL, 0);
INSERT INTO `assistive_technology` (`assistive_technology_id`, `assistive_technology_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (33, 7, 'amformas.recruit.ans.sr_N_braille_id.6', 'Handy Tech Braille display', NULL, 0);
INSERT INTO `assistive_technology` (`assistive_technology_id`, `assistive_technology_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (34, 7, 'amformas.recruit.ans.sr_N_braille_id.7', 'Hedo Braille display', NULL, 0);
INSERT INTO `assistive_technology` (`assistive_technology_id`, `assistive_technology_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (35, 7, 'amformas.recruit.ans.sr_N_braille_id.8', 'Visiobraille Braille display', NULL, 0);
INSERT INTO `assistive_technology` (`assistive_technology_id`, `assistive_technology_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (36, 7, 'amformas.recruit.ans.sr_N_braille_id.9', 'Focus 40 Braille Display', NULL, 0);
INSERT INTO `assistive_technology` (`assistive_technology_id`, `assistive_technology_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (37, 7, 'amformas.recruit.ans.sr_N_braille_id.10', 'Focus 80 Braille Display', NULL, 0);
INSERT INTO `assistive_technology` (`assistive_technology_id`, `assistive_technology_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (38, 7, 'amformas.recruit.ans.sr_N_braille_id.11', 'PAC Mate 20 Portable Braille Display', NULL, 0);
INSERT INTO `assistive_technology` (`assistive_technology_id`, `assistive_technology_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (39, 7, 'amformas.recruit.ans.sr_N_braille_id.12', 'PAC Mate 40 Portable Braille Display', NULL, 0);
INSERT INTO `assistive_technology` (`assistive_technology_id`, `assistive_technology_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (40, 8, 'amformas.recruit.ans.alterin_id.1', 'switch and scanning device', NULL, 1);
INSERT INTO `assistive_technology` (`assistive_technology_id`, `assistive_technology_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (41, 8, 'amformas.recruit.ans.alterin_id.2', 'alternative keyboard', NULL, 1);
INSERT INTO `assistive_technology` (`assistive_technology_id`, `assistive_technology_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (42, 8, 'amformas.recruit.ans.alterin_id.3', 'headmouse / tracking device', NULL, 1);
INSERT INTO `assistive_technology` (`assistive_technology_id`, `assistive_technology_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (43, 8, 'amformas.recruit.ans.alterin_id.4', 'CCTV (undefined product)', NULL, 0);
INSERT INTO `assistive_technology` (`assistive_technology_id`, `assistive_technology_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (44, 8, 'amformas.recruit.ans.alterin_id.5', 'HMC EasyMouse', NULL, 0);
INSERT INTO `assistive_technology` (`assistive_technology_id`, `assistive_technology_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (45, 8, 'amformas.recruit.ans.alterin_id.6', 'Head pointer', NULL, 0);
INSERT INTO `assistive_technology` (`assistive_technology_id`, `assistive_technology_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (46, 8, 'amformas.recruit.ans.alterin_id.7', 'Lucy laser keyboard', NULL, 0);
INSERT INTO `assistive_technology` (`assistive_technology_id`, `assistive_technology_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (47, 8, 'amformas.recruit.ans.alterin_id.8', 'Maltron one-handed keyboard', NULL, 0);
INSERT INTO `assistive_technology` (`assistive_technology_id`, `assistive_technology_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (48, 1, 'amformas.recruit.ans.altern_other.1', 'desktop video magnifier', NULL, 0);
INSERT INTO `assistive_technology` (`assistive_technology_id`, `assistive_technology_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (49, 1, 'amformas.recruit.ans.altern_other.2', 'portable video magnifier', NULL, 0);
INSERT INTO `assistive_technology` (`assistive_technology_id`, `assistive_technology_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (50, 1, 'amformas.recruit.ans.altern_other.3', 'video magnifier that plugs into a television', NULL, 0);
INSERT INTO `assistive_technology` (`assistive_technology_id`, `assistive_technology_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (51, 1, 'amformas.recruit.ans.altern_other.4', 'PC sharing video magnifier', NULL, 0);

-- 
-- Dumping data for table `language`
-- 

INSERT INTO `language` (`language_id`, `name_key`, `name_default`, `shortcode`) VALUES (1, 'amformas.recruit.ans.language_id.0', NULL, NULL);
INSERT INTO `language` (`language_id`, `name_key`, `name_default`, `shortcode`) VALUES (2, 'amformas.recruit.ans.language_id.1', 'Dutch', NULL);
INSERT INTO `language` (`language_id`, `name_key`, `name_default`, `shortcode`) VALUES (3, 'amformas.recruit.ans.language_id.2', 'English', NULL);
INSERT INTO `language` (`language_id`, `name_key`, `name_default`, `shortcode`) VALUES (4, 'amformas.recruit.ans.language_id.3', 'French', NULL);
INSERT INTO `language` (`language_id`, `name_key`, `name_default`, `shortcode`) VALUES (5, 'amformas.recruit.ans.language_id.4', 'German', NULL);
INSERT INTO `language` (`language_id`, `name_key`, `name_default`, `shortcode`) VALUES (6, 'amformas.recruit.ans.language_id.5', 'Greek', NULL);
INSERT INTO `language` (`language_id`, `name_key`, `name_default`, `shortcode`) VALUES (7, 'amformas.recruit.ans.language_id.6', 'Italian', NULL);
INSERT INTO `language` (`language_id`, `name_key`, `name_default`, `shortcode`) VALUES (8, 'amformas.recruit.ans.language_id.7', 'Spanish', NULL);

-- 
-- Dumping data for table `language_experience`
-- 

INSERT INTO `language_experience` (`language_experience_id`, `name_key`, `name_default`) VALUES (1, 'amformas.recruit.ans.common.good_id.0', NULL);
INSERT INTO `language_experience` (`language_experience_id`, `name_key`, `name_default`) VALUES (2, 'amformas.recruit.ans.common.good_id.1', 'Not at all good');
INSERT INTO `language_experience` (`language_experience_id`, `name_key`, `name_default`) VALUES (3, 'amformas.recruit.ans.common.good_id.2', 'Not very good');
INSERT INTO `language_experience` (`language_experience_id`, `name_key`, `name_default`) VALUES (4, 'amformas.recruit.ans.common.good_id.3', 'Good');
INSERT INTO `language_experience` (`language_experience_id`, `name_key`, `name_default`) VALUES (5, 'amformas.recruit.ans.common.good_id.4', 'Quite good');
INSERT INTO `language_experience` (`language_experience_id`, `name_key`, `name_default`) VALUES (6, 'amformas.recruit.ans.common.good_id.5', 'Very good');

-- 
-- Dumping data for table `operating_system`
-- 

INSERT INTO `operating_system` (`operationg_system_id`, `name_key`, `name_default`, `vendor`) VALUES (1, 'amformas.recruit.ans.common.other', NULL, NULL);
INSERT INTO `operating_system` (`operationg_system_id`, `name_key`, `name_default`, `vendor`) VALUES (2, 'amformas.recruit.ans.user_uses_os_id.win9', 'Microsoft Windows 9.X', NULL);
INSERT INTO `operating_system` (`operationg_system_id`, `name_key`, `name_default`, `vendor`) VALUES (3, 'amformas.recruit.ans.user_uses_os_id.windows_nt', 'Microsoft Windows NT', NULL);
INSERT INTO `operating_system` (`operationg_system_id`, `name_key`, `name_default`, `vendor`) VALUES (4, 'amformas.recruit.ans.user_uses_os_id.mac', 'Macintosh OS', NULL);
INSERT INTO `operating_system` (`operationg_system_id`, `name_key`, `name_default`, `vendor`) VALUES (5, 'amformas.recruit.ans.user_uses_os_id.linux', 'Linux', NULL);
INSERT INTO `operating_system` (`operationg_system_id`, `name_key`, `name_default`, `vendor`) VALUES (6, 'amformas.recruit.ans.user_uses_os_id.unix', 'Unix', NULL);

-- 
-- Dumping data for table `operating_system_setting`
-- 

INSERT INTO `operating_system_setting` (`operating_system_setting_id`, `name_key`, `name_default`) VALUES (1, 'amformas.recruit.ans.os_settings_id.0', NULL);
INSERT INTO `operating_system_setting` (`operating_system_setting_id`, `name_key`, `name_default`) VALUES (2, 'amformas.recruit.ans.os_settings_id.1', 'Text size');
INSERT INTO `operating_system_setting` (`operating_system_setting_id`, `name_key`, `name_default`) VALUES (3, 'amformas.recruit.ans.os_settings_id.2', 'Colour scheme');
INSERT INTO `operating_system_setting` (`operating_system_setting_id`, `name_key`, `name_default`) VALUES (4, 'amformas.recruit.ans.os_settings_id.3', 'Screen resolution');

-- 
-- Dumping data for table `sex`
-- 

INSERT INTO `sex` (`sex_id`, `name_key`, `name_default`) VALUES (1, 'amformas.recruit.ans.sex.null', NULL);
INSERT INTO `sex` (`sex_id`, `name_key`, `name_default`) VALUES (2, 'amformas.recruit.ans.sex.male', 'Male');
INSERT INTO `sex` (`sex_id`, `name_key`, `name_default`) VALUES (3, 'amformas.recruit.ans.sex.female', 'Female');

-- 
-- Dumping data for table `test_suite`
-- 

INSERT INTO `test_suite` (`test_suite_id`, `title`, `description`, `index_file_uri`, `metadata_files_uri`, `test_files_uri`, `mapping_file_uri`, `timesPerTestCase`, `testCasesPerUser`, `date_start`, `date_end`, `is_active`, `object`) VALUES (1, 'XHTML1', '', 'http://www.bentoweb.org/ts/XHTML1/home.xml', 'http://bentoweb.org/ts/XHTML1/metadata/', 'http://bentoweb.org/ts/XHTML1/testfiles/', 'context://amfortas/resources/tcmapping/amml.xml', 5, 20, NULL, NULL, 1, NULL);


-- 
-- Dumping data for table `user_agent_types`
-- 

INSERT INTO `user_agent_types` (`user_agent_types_id`, `name_key`, `name_default`) VALUES (1, 'amformas.recruit.ans.user_agent_types.null', NULL);
INSERT INTO `user_agent_types` (`user_agent_types_id`, `name_key`, `name_default`) VALUES (2, 'amformas.recruit.ans.user_agent_types.browser', 'browser');
INSERT INTO `user_agent_types` (`user_agent_types_id`, `name_key`, `name_default`) VALUES (3, 'amformas.recruit.ans.user_agent_types.talking_browser', 'talking browser');
INSERT INTO `user_agent_types` (`user_agent_types_id`, `name_key`, `name_default`) VALUES (4, 'amformas.recruit.ans.user_agent_types.other', 'other');


-- 
-- Dumping data for table `user_agent`
-- 

INSERT INTO `user_agent` (`user_agent_id`, `user_agent_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (1, 1, 'amformas.recruit.ans.user_agent.null', NULL, NULL, 0);
INSERT INTO `user_agent` (`user_agent_id`, `user_agent_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (2, 1, 'amformas.recruit.ans.common.none', 'None', NULL, 1);
INSERT INTO `user_agent` (`user_agent_id`, `user_agent_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (3, 1, 'amformas.recruit.ans.common.notknow', 'Don''t know', NULL, 1);
INSERT INTO `user_agent` (`user_agent_id`, `user_agent_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (4, 1, 'amformas.recruit.ans.common.other', 'Other (please state below)', NULL, 1);
INSERT INTO `user_agent` (`user_agent_id`, `user_agent_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (5, 2, 'amformas.recruit.ans.user_agent.msie', 'Microsoft Internet Explorer', NULL, 1);
INSERT INTO `user_agent` (`user_agent_id`, `user_agent_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (6, 2, 'amformas.recruit.ans.user_agent.netscape', 'Netscape', NULL, 1);
INSERT INTO `user_agent` (`user_agent_id`, `user_agent_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (7, 2, 'amformas.recruit.ans.user_agent.firefox', 'Firefox', NULL, 1);
INSERT INTO `user_agent` (`user_agent_id`, `user_agent_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (8, 2, 'amformas.recruit.ans.user_agent.opera', 'Opera', NULL, 1);
INSERT INTO `user_agent` (`user_agent_id`, `user_agent_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (9, 2, 'amformas.recruit.ans.user_agent.safari', 'Safari', NULL, 1);
INSERT INTO `user_agent` (`user_agent_id`, `user_agent_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (10, 2, 'amformas.recruit.ans.user_agent.amaya', 'Amaya', NULL, 0);
INSERT INTO `user_agent` (`user_agent_id`, `user_agent_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (11, 2, 'amformas.recruit.ans.user_agent.camino', 'Camino', NULL, 0);
INSERT INTO `user_agent` (`user_agent_id`, `user_agent_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (12, 2, 'amformas.recruit.ans.user_agent.chimera', 'Chimera', NULL, 0);
INSERT INTO `user_agent` (`user_agent_id`, `user_agent_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (13, 2, 'amformas.recruit.ans.user_agent.clue', 'Clue', NULL, 0);
INSERT INTO `user_agent` (`user_agent_id`, `user_agent_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (14, 2, 'amformas.recruit.ans.user_agent.flock', 'Flock', NULL, 0);
INSERT INTO `user_agent` (`user_agent_id`, `user_agent_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (15, 2, 'amformas.recruit.ans.user_agent.galeon', 'Galeon', NULL, 0);
INSERT INTO `user_agent` (`user_agent_id`, `user_agent_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (16, 2, 'amformas.recruit.ans.user_agent.hotjava', 'HotJava', NULL, 0);
INSERT INTO `user_agent` (`user_agent_id`, `user_agent_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (17, 2, 'amformas.recruit.ans.user_agent.icab', 'iCab', NULL, 0);
INSERT INTO `user_agent` (`user_agent_id`, `user_agent_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (18, 2, 'amformas.recruit.ans.user_agent.kmeleon', 'K-Meleon', NULL, 0);
INSERT INTO `user_agent` (`user_agent_id`, `user_agent_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (19, 2, 'amformas.recruit.ans.user_agent.konqueror', 'Konqueror', NULL, 0);
INSERT INTO `user_agent` (`user_agent_id`, `user_agent_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (20, 2, 'amformas.recruit.ans.user_agent.links', 'Links', NULL, 0);
INSERT INTO `user_agent` (`user_agent_id`, `user_agent_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (21, 2, 'amformas.recruit.ans.user_agent.lynx', 'Lynx', NULL, 0);
INSERT INTO `user_agent` (`user_agent_id`, `user_agent_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (22, 2, 'amformas.recruit.ans.user_agent.mozilla', 'Mozilla', NULL, 0);
INSERT INTO `user_agent` (`user_agent_id`, `user_agent_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (23, 2, 'amformas.recruit.ans.user_agent.nautilus', 'Nautilus', NULL, 0);
INSERT INTO `user_agent` (`user_agent_id`, `user_agent_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (24, 2, 'amformas.recruit.ans.user_agent.netscape_navigator', 'Netscape Navigator', NULL, 0);
INSERT INTO `user_agent` (`user_agent_id`, `user_agent_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (25, 2, 'amformas.recruit.ans.user_agent.shiira', 'Shiira', NULL, 0);
INSERT INTO `user_agent` (`user_agent_id`, `user_agent_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (26, 2, 'amformas.recruit.ans.user_agent.xsmiles', 'X-Smiles', NULL, 0);
INSERT INTO `user_agent` (`user_agent_id`, `user_agent_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (27, 3, 'amformas.recruit.ans.user_agent.homepage_reader', 'IBM Home Page Reader', NULL, 1);
INSERT INTO `user_agent` (`user_agent_id`, `user_agent_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (28, 3, 'amformas.recruit.ans.user_agent.freedombox', 'FreedomBox', NULL, 1);
INSERT INTO `user_agent` (`user_agent_id`, `user_agent_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (29, 3, 'amformas.recruit.ans.user_agent.edweb', 'EdWeb', NULL, 1);
INSERT INTO `user_agent` (`user_agent_id`, `user_agent_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (30, 3, 'amformas.recruit.ans.user_agent.braillesurf', 'BrailleSurf', NULL, 0);
INSERT INTO `user_agent` (`user_agent_id`, `user_agent_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (31, 3, 'amformas.recruit.ans.user_agent.emacspeak', 'EmacSpeak', NULL, 0);
INSERT INTO `user_agent` (`user_agent_id`, `user_agent_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (32, 3, 'amformas.recruit.ans.user_agent.multiweb', 'MultiWeb', NULL, 0);
INSERT INTO `user_agent` (`user_agent_id`, `user_agent_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (33, 3, 'amformas.recruit.ans.user_agent.pwwebspeak', 'pwWebSpeak', NULL, 0);
INSERT INTO `user_agent` (`user_agent_id`, `user_agent_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (34, 3, 'amformas.recruit.ans.user_agent.sensus_internet_browser', 'Sensus Internet Browser', NULL, 0);
INSERT INTO `user_agent` (`user_agent_id`, `user_agent_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (35, 3, 'amformas.recruit.ans.user_agent.simply_web_2000', 'Simply Web 2000', NULL, 0);
INSERT INTO `user_agent` (`user_agent_id`, `user_agent_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (36, 3, 'amformas.recruit.ans.user_agent.webbie', 'WebbIE', NULL, 0);
INSERT INTO `user_agent` (`user_agent_id`, `user_agent_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (37, 4, 'amformas.recruit.ans.user_agent.adobe_acrobat_reader', 'Adobe Acrobat Reader', NULL, 0);
INSERT INTO `user_agent` (`user_agent_id`, `user_agent_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (38, 4, 'amformas.recruit.ans.user_agent.adobe_svg_viewer', 'Adobe SVG Viewer', NULL, 0);
INSERT INTO `user_agent` (`user_agent_id`, `user_agent_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (39, 4, 'amformas.recruit.ans.user_agent.apple_quicktime', 'Apple QuickTime', NULL, 0);
INSERT INTO `user_agent` (`user_agent_id`, `user_agent_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (40, 4, 'amformas.recruit.ans.user_agent.corel_svg_viewer', 'Corel SVG Viewer', NULL, 0);
INSERT INTO `user_agent` (`user_agent_id`, `user_agent_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (41, 4, 'amformas.recruit.ans.user_agent.formsplayer', 'formsPlayer', NULL, 0);
INSERT INTO `user_agent` (`user_agent_id`, `user_agent_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (42, 4, 'amformas.recruit.ans.user_agent.macromedia_flash_player', 'Macromedia Flash Player', NULL, 0);
INSERT INTO `user_agent` (`user_agent_id`, `user_agent_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (43, 4, 'amformas.recruit.ans.user_agent.macromedia_shockwave', 'Macromedia Shockwave', NULL, 0);
INSERT INTO `user_agent` (`user_agent_id`, `user_agent_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (44, 4, 'amformas.recruit.ans.user_agent.realnetworks_realplayer', 'RealNetworks RealPlayer', NULL, 0);
INSERT INTO `user_agent` (`user_agent_id`, `user_agent_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (45, 4, 'amformas.recruit.ans.user_agent.sun_java-plugin', 'Sun Java Plug-in', NULL, 0);
INSERT INTO `user_agent` (`user_agent_id`, `user_agent_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (46, 4, 'amformas.recruit.ans.user_agent.mathplayer', 'MathPlayer', NULL, 0);
INSERT INTO `user_agent` (`user_agent_id`, `user_agent_types_id`, `name_key`, `name_default`, `vendor`, `visible`) VALUES (47, 4, 'amformas.recruit.ans.user_agent.windows_media_player', 'Windows Media Player', NULL, 0);

-- 
-- Dumping data for table `user_agent_setting`
-- 

INSERT INTO `user_agent_setting` (`user_agent_setting_id`, `name_key`, `name_default`) VALUES (1, 'amformas.recruit.ans.user_agent_setting_id.0', NULL);
INSERT INTO `user_agent_setting` (`user_agent_setting_id`, `name_key`, `name_default`) VALUES (2, 'amformas.recruit.ans.user_agent_setting_id.1', 'Text font (i.e. Arial)');
INSERT INTO `user_agent_setting` (`user_agent_setting_id`, `name_key`, `name_default`) VALUES (3, 'amformas.recruit.ans.user_agent_setting_id.2', 'Text size');
INSERT INTO `user_agent_setting` (`user_agent_setting_id`, `name_key`, `name_default`) VALUES (4, 'amformas.recruit.ans.user_agent_setting_id.3', 'Text colour');
INSERT INTO `user_agent_setting` (`user_agent_setting_id`, `name_key`, `name_default`) VALUES (5, 'amformas.recruit.ans.user_agent_setting_id.4', 'Background colour');

-- 
-- Dumping data for table `user`
-- 

INSERT INTO `user` (`user_id`, `sex_id`, `language_id`, `password`, `name_first`, `name_last`, `email`, `language_native_other`, `date_register`, `status_user`, `mail_conf_hash`) VALUES (1, 2, 8, '', 'Amfortas', 'admin', 'admin@example.org', NULL, '2006-01-21 12:23:58', 2, '58lKSTwtMqShqyfID4ikjUtjkro=');
INSERT INTO `user` (`user_id`, `sex_id`, `language_id`, `password`, `name_first`, `name_last`, `email`, `language_native_other`, `date_register`, `status_user`, `mail_conf_hash`) VALUES (2, 2, 8, '', 'Amfortas', 'user', 'user@example.org', NULL, '2006-01-21 12:23:58', 2, 'pCAYefAa1i9zGy29Wf4gLR1aA84=');
INSERT INTO `user` (`user_id`, `sex_id`, `language_id`, `password`, `name_first`, `name_last`, `email`, `language_native_other`, `date_register`, `status_user`, `mail_conf_hash`) VALUES (3, 2, 8, '', 'Amfortas', 'anon', 'anon@example.org', NULL, '2006-01-21 12:23:58', 2, 'ls68smVklesUdYOTq169EcLyXvg=');

-- 
-- Dumping data for table `role`
-- 

INSERT INTO `role` (`role_id`, `name_key`, `name_default`) VALUES (1, 'amfortas.user.role.admin', 'Administrator');
INSERT INTO `role` (`role_id`, `name_key`, `name_default`) VALUES (2, 'amfortas.user.role.user', 'Simple User');
INSERT INTO `role` (`role_id`, `name_key`, `name_default`) VALUES (3, 'amfortas.user.role.anon', 'Anonymous');

-- 
-- Dumping data for table `user_has_role`
-- 

INSERT INTO `user_has_role` (`user_id`, `role_id`) VALUES (1, 1);
INSERT INTO `user_has_role` (`user_id`, `role_id`) VALUES (2, 2);
INSERT INTO `user_has_role` (`user_id`, `role_id`) VALUES (3, 3);
