/*
Navicat MySQL Data Transfer

Source Server         : aliyun
Source Server Version : 50722
Source Host           : 39.105.81.7:3306
Source Database       : share

Target Server Type    : MYSQL
Target Server Version : 50722
File Encoding         : 65001

Date: 2018-11-29 20:46:53
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `s_albums`
-- ----------------------------
DROP TABLE IF EXISTS `s_albums`;
CREATE TABLE `s_albums` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `create_ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `album_title` text,
  `album_desc` text COMMENT '描述',
  `last_add_ts` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `photos_count` int(11) NOT NULL DEFAULT '0',
  `status` int(11) NOT NULL DEFAULT '0',
  `cover` varchar(300) DEFAULT NULL,
  `album_tags` text,
  PRIMARY KEY (`id`),
  KEY `fk_osf_albums_album_author_idx` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of s_albums
-- ----------------------------
INSERT INTO `s_albums` VALUES ('1', '2', '2018-11-29 17:50:29', null, '1111', '2018-11-29 17:50:29', '0', '0', '/upload/c/b/6/1/0/e/7/b/07f2c27e-58b9-45a0-9a23-7467722fae8c.jpg', null);
INSERT INTO `s_albums` VALUES ('2', '3', '2018-11-29 18:39:47', null, '下一个死的就是你', '2018-11-29 18:39:47', '0', '0', '/upload/4/4/1/7/2/e/8/f/8c976115-4796-4fd2-971b-4a4ece98ad25.jpg', null);
INSERT INTO `s_albums` VALUES ('3', '3', '2018-11-29 19:47:31', null, '不好', '2018-11-29 19:47:31', '0', '0', '/upload/e/2/a/d/b/9/3/7/04b0c12d-d23b-40d3-b11a-d45136dc1fac.jpg', null);
INSERT INTO `s_albums` VALUES ('4', '6', '2018-11-29 19:57:47', null, '女神！', '2018-11-29 19:57:47', '0', '0', '/upload/e/3/7/d/c/1/8/d/91bb063a-7e55-435a-a466-5009cf94b31c.jpg', null);
INSERT INTO `s_albums` VALUES ('5', '7', '2018-11-29 19:58:28', null, '海底', '2018-11-29 19:58:28', '0', '0', '/upload/b/4/8/0/0/2/a/8/4b3a957a-e8ce-464b-aa63-217a64ffe1da.jpg', null);
INSERT INTO `s_albums` VALUES ('6', '5', '2018-11-29 20:10:50', null, null, '2018-11-29 20:10:50', '0', '1', null, null);
INSERT INTO `s_albums` VALUES ('7', '5', '2018-11-29 20:13:50', null, '#余生', '2018-11-29 20:13:50', '0', '0', '/upload/2/c/9/0/4/a/4/0/642fc684-76d1-49a9-b412-e735338aa261.jpg', null);
INSERT INTO `s_albums` VALUES ('8', '6', '2018-11-29 20:26:46', null, null, '2018-11-29 20:26:46', '0', '1', null, null);
INSERT INTO `s_albums` VALUES ('9', '6', '2018-11-29 20:34:12', null, null, '2018-11-29 20:34:12', '0', '1', null, null);

-- ----------------------------
-- Table structure for `s_comments`
-- ----------------------------
DROP TABLE IF EXISTS `s_comments`;
CREATE TABLE `s_comments` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `comment_object_type` int(11) NOT NULL COMMENT 'post, album,...',
  `comment_object_id` int(11) NOT NULL,
  `comment_author` int(11) NOT NULL,
  `comment_author_name` varchar(100) NOT NULL,
  `comment_ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `comment_content` text NOT NULL,
  `comment_parent` int(11) NOT NULL DEFAULT '0',
  `comment_parent_author_name` varchar(100) DEFAULT NULL,
  `comment_parent_author` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `fk_osf_comments_comment_author_idx` (`comment_author`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of s_comments
-- ----------------------------
INSERT INTO `s_comments` VALUES ('1', '2', '2', '3', '超', '2018-11-29 18:42:25', '666', '0', null, '0');
INSERT INTO `s_comments` VALUES ('2', '1', '2', '5', '杰杰', '2018-11-29 19:33:31', '这个歌柯南', '0', null, '0');
INSERT INTO `s_comments` VALUES ('3', '2', '2', '5', '杰杰', '2018-11-29 19:34:30', '哇哦考', '0', null, '0');
INSERT INTO `s_comments` VALUES ('4', '0', '1', '4', '李飒', '2018-11-29 20:22:06', '。。。。', '0', null, '0');
INSERT INTO `s_comments` VALUES ('5', '0', '1', '5', '于洁', '2018-11-29 20:26:15', '哼~', '4', '李飒', '4');

-- ----------------------------
-- Table structure for `s_events`
-- ----------------------------
DROP TABLE IF EXISTS `s_events`;
CREATE TABLE `s_events` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `object_type` int(11) NOT NULL,
  `object_id` int(11) NOT NULL,
  `ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `user_id` int(11) NOT NULL,
  `user_name` varchar(100) DEFAULT NULL,
  `user_avatar` varchar(100) DEFAULT NULL,
  `like_count` int(11) NOT NULL,
  `share_count` int(11) NOT NULL,
  `comment_count` int(11) NOT NULL,
  `title` text,
  `summary` text,
  `content` text,
  `tags` text,
  `following_user_id` int(11) DEFAULT NULL,
  `following_user_name` varchar(50) DEFAULT NULL,
  `follower_user_id` int(11) DEFAULT NULL,
  `follower_user_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of s_events
-- ----------------------------
INSERT INTO `s_events` VALUES ('1', '4', '1', '2018-11-29 17:49:45', '2', null, null, '0', '0', '0', null, '我是郭超的爸爸', null, null, '0', null, '0', null);
INSERT INTO `s_events` VALUES ('2', '2', '1', '2018-11-29 17:50:36', '2', null, null, '0', '0', '0', '/upload/c/b/6/1/0/e/7/b/07f2c27e-58b9-45a0-9a23-7467722fae8c.jpg', '1111', '/upload/c/b/6/1/0/e/7/b/07f2c27e-58b9-45a0-9a23-7467722fae8c.jpg:', null, '0', null, '0', null);
INSERT INTO `s_events` VALUES ('3', '4', '1', '2018-11-29 18:39:28', '3', null, null, '0', '0', '0', null, '周四晚。完成了，嗨皮<img class=\"emojione\" alt=\"\" src=\"http://www.share.com/img/emoji/1F600.png?v=1.2.4\"/><img class=\"emojione\" alt=\"\" src=\"http://www.share.com/img/emoji/1F600.png?v=1.2.4\"/><img class=\"emojione\" alt=\"\" src=\"http://www.share.com/img/emoji/1F600.png?v=1.2.4\"/><img class=\"emojione\" alt=\"\" src=\"http://www.share.com/img/emoji/1F600.png?v=1.2.4\"/><img class=\"emojione\" alt=\"\" src=\"http://www.share.com/img/emoji/1F600.png?v=1.2.4\"/><img class=\"emojione\" alt=\"\" src=\"http://www.share.com/img/emoji/1F600.png?v=1.2.4\"/>', null, null, '0', null, '0', null);
INSERT INTO `s_events` VALUES ('4', '2', '2', '2018-11-29 18:40:22', '3', null, null, '0', '0', '0', '/upload/4/4/1/7/2/e/8/f/8c976115-4796-4fd2-971b-4a4ece98ad25.jpg', '下一个死的就是你', '/upload/4/4/1/7/2/e/8/f/8c976115-4796-4fd2-971b-4a4ece98ad25.jpg:', null, '0', null, '0', null);
INSERT INTO `s_events` VALUES ('5', '2', '3', '2018-11-29 19:47:46', '3', null, null, '0', '0', '0', '/upload/a/3/f/5/b/3/d/e/398df2d0-8394-4957-b403-81df02055db3.jpg', '6', '/upload/a/3/f/5/b/3/d/e/398df2d0-8394-4957-b403-81df02055db3.jpg:/upload/1/5/a/d/c/3/f/f/a2c16d46-5789-4d0d-8818-5e3d8db35bb9.jpg:', null, '0', null, '0', null);
INSERT INTO `s_events` VALUES ('6', '2', '3', '2018-11-29 19:48:01', '3', null, null, '0', '0', '0', '/upload/e/2/a/d/b/9/3/7/04b0c12d-d23b-40d3-b11a-d45136dc1fac.jpg', '不好', '/upload/e/2/a/d/b/9/3/7/04b0c12d-d23b-40d3-b11a-d45136dc1fac.jpg:', null, '0', null, '0', null);
INSERT INTO `s_events` VALUES ('7', '0', '1', '2018-11-29 19:48:35', '3', null, null, '0', '0', '0', '库里车祸', '惨啊惨啊', null, null, '0', null, '0', null);
INSERT INTO `s_events` VALUES ('8', '4', '1', '2018-11-29 19:50:26', '3', null, null, '0', '0', '0', null, '从小生活在江西，长大离开家后，格外想念家乡的那片红土地', null, null, '0', null, '0', null);
INSERT INTO `s_events` VALUES ('9', '4', '1', '2018-11-29 19:50:43', '3', null, null, '0', '0', '0', null, '【因老伴生前一句话，他坚持5年抄完四大名著】5年前，江苏南京老人王振顺的老伴因心梗突然去世，生前曾嘱托他以后多写点字。于是，5年来，身患帕金森氏综合征的王振顺即使眼睛模糊了，笔也拿不稳了，仍坚持每天写字。328万字、3815张宣纸……今年10月，他抄完四大名著：老伴布置的作业，一定要完成', null, null, '0', null, '0', null);
INSERT INTO `s_events` VALUES ('10', '4', '1', '2018-11-29 19:50:52', '3', null, null, '0', '0', '0', null, '央视新闻”微博是中央电视台新闻中心官方微博，是央视重大新闻、突发事件、重点报道的首发平台。<img class=\"emojione\" alt=\"\" src=\"http://www.share.com/img/emoji/1F604.png?v=1.2.4\"/>', null, null, '0', null, '0', null);
INSERT INTO `s_events` VALUES ('11', '4', '1', '2018-11-29 19:51:04', '3', null, null, '0', '0', '0', null, '258人违规、21人永久禁赛！深圳南山半马大量参赛者作弊被罚<img class=\"emojione\" alt=\"\" src=\"http://www.share.com/img/emoji/1F603.png?v=1.2.4\"/><img class=\"emojione\" alt=\"\" src=\"http://www.share.com/img/emoji/1F603.png?v=1.2.4\"/><img class=\"emojione\" alt=\"\" src=\"http://www.share.com/img/emoji/1F603.png?v=1.2.4\"/><img class=\"emojione\" alt=\"\" src=\"http://www.share.com/img/emoji/1F603.png?v=1.2.4\"/>', null, null, '0', null, '0', null);
INSERT INTO `s_events` VALUES ('12', '0', '1', '2018-11-29 19:51:42', '3', null, null, '0', '0', '0', '258人违规、21人永久禁赛！深圳南山半马大量参赛者作弊被罚', '上周日深圳南山一场热热闹闹的半程马拉松赛 成了这几天令人“尴尬”的话题 南山半马被曝集体抄近路作弊、大面积套牌 赛事组委会发布处理意见公告 28日，赛事组委会发布对违规选手处理意见的公告，根据计时芯片数据、赛会录像、现场照片等材料证明，总计违规选手人数258人，其中伪造号码布18人，替跑3人。 按照《2018深圳南山半程马拉松竞赛规程》第7-8条规定： 伪造号码布及替跑选手将被永久禁止参加深圳南山', null, null, '0', null, '0', null);
INSERT INTO `s_events` VALUES ('13', '4', '1', '2018-11-29 19:56:51', '6', null, null, '0', '0', '0', null, '周五国安要夺冠！', null, null, '0', null, '0', null);
INSERT INTO `s_events` VALUES ('14', '2', '4', '2018-11-29 19:58:27', '6', null, null, '0', '0', '0', '/upload/4/8/1/5/5/e/b/a/5f350fc2-f9ba-465d-9333-9f5a99140284.jpg', '上联：北京欢迎你！\n下联：国安是冠军！\n横批：牛逼！', '/upload/4/8/1/5/5/e/b/a/5f350fc2-f9ba-465d-9333-9f5a99140284.jpg:', null, '0', null, '0', null);
INSERT INTO `s_events` VALUES ('15', '2', '5', '2018-11-29 19:58:48', '7', null, null, '0', '0', '0', '/upload/c/b/6/1/0/e/7/b/3f48febf-cd89-45e7-b9cf-1997a81b7535.jpg', '666', '/upload/c/b/6/1/0/e/7/b/3f48febf-cd89-45e7-b9cf-1997a81b7535.jpg:', null, '0', null, '0', null);
INSERT INTO `s_events` VALUES ('16', '0', '1', '2018-11-29 20:00:14', '6', null, null, '0', '0', '0', '美丽的一天', '大家好，我叫郭美丽。。。', null, null, '0', null, '0', null);
INSERT INTO `s_events` VALUES ('17', '0', '1', '2018-11-29 20:00:25', '7', null, null, '0', '0', '0', '百度新闻', '新华社北京11月29日电　国家主席习近平同俄罗斯总统普京29日分别向中俄能源商务论坛致贺信。 　　习近平在贺信中指出，当前，中俄全面战略协作伙伴关系保持高水平运行，各领域合作不断发展。近年来，两国能源合作取得了丰硕成果，为促进两国经济社会发展发挥了积极作用。中方愿同俄方一道，共同推动中俄能源合作再上新台阶。 　　习近平强调，举办中俄能源商务论坛是我和普京总统达成的重要共识，旨在为双方企业搭建直接对', null, null, '0', null, '0', null);
INSERT INTO `s_events` VALUES ('18', '4', '1', '2018-11-29 20:00:37', '7', null, null, '0', '0', '0', null, '<img class=\"emojione\" alt=\"\" src=\"http://www.share.com/img/emoji/1F602.png?v=1.2.4\"/><img class=\"emojione\" alt=\"\" src=\"http://www.share.com/img/emoji/1F602.png?v=1.2.4\"/><img class=\"emojione\" alt=\"\" src=\"http://www.share.com/img/emoji/1F602.png?v=1.2.4\"/><img class=\"emojione\" alt=\"\" src=\"http://www.share.com/img/emoji/1F603.png?v=1.2.4\"/><img class=\"emojione\" alt=\"\" src=\"http://www.share.com/img/emoji/1F603.png?v=1.2.4\"/><img class=\"emojione\" alt=\"\" src=\"http://www.share.com/img/emoji/1F603.png?v=1.2.4\"/>', null, null, '0', null, '0', null);
INSERT INTO `s_events` VALUES ('19', '0', '1', '2018-11-29 20:02:10', '7', null, null, '0', '0', '0', '今日头条', '中国新闻网是知名的中文新闻门户网站,也是全球互联网中文新闻资讯最重要的原创内容供应商之一。依托中新社遍布全球的采编网络,每天24小时面向广大网民和网络媒体 《今日头条》(www.toutiao.com)是一款基于数据挖掘的推荐引擎产品,它为用户推荐有价值的、个性化的信息,提供连接人与信息的新型服务,是国内移动互联网领域成长最快', null, null, '0', null, '0', null);
INSERT INTO `s_events` VALUES ('20', '2', '4', '2018-11-29 20:02:21', '6', null, null, '0', '0', '0', '/upload/e/3/7/d/c/1/8/d/91bb063a-7e55-435a-a466-5009cf94b31c.jpg', '女神！', '/upload/e/3/7/d/c/1/8/d/91bb063a-7e55-435a-a466-5009cf94b31c.jpg:', null, '0', null, '0', null);
INSERT INTO `s_events` VALUES ('21', '2', '5', '2018-11-29 20:02:36', '7', null, null, '0', '0', '0', '/upload/b/4/8/0/0/2/a/8/4b3a957a-e8ce-464b-aa63-217a64ffe1da.jpg', '海底', '/upload/b/4/8/0/0/2/a/8/4b3a957a-e8ce-464b-aa63-217a64ffe1da.jpg:/upload/c/e/f/9/4/9/0/9/ccc64b70-3ca3-4acf-b1de-5d235b01a9c8.jpg:', null, '0', null, '0', null);
INSERT INTO `s_events` VALUES ('22', '4', '1', '2018-11-29 20:03:03', '2', null, null, '0', '0', '0', null, '终于完成了', null, null, '0', null, '0', null);
INSERT INTO `s_events` VALUES ('23', '4', '1', '2018-11-29 20:03:10', '2', null, null, '0', '0', '0', null, '<img class=\"emojione\" alt=\"\" src=\"http://www.share.com/img/emoji/1F600.png?v=1.2.4\"/><img class=\"emojione\" alt=\"\" src=\"http://www.share.com/img/emoji/1F601.png?v=1.2.4\"/><img class=\"emojione\" alt=\"\" src=\"http://www.share.com/img/emoji/1F601.png?v=1.2.4\"/><img class=\"emojione\" alt=\"\" src=\"http://www.share.com/img/emoji/1F601.png?v=1.2.4\"/><img class=\"emojione\" alt=\"\" src=\"http://www.share.com/img/emoji/1F601.png?v=1.2.4\"/><img class=\"emojione\" alt=\"\" src=\"http://www.share.com/img/emoji/1F601.png?v=1.2.4\"/>', null, null, '0', null, '0', null);
INSERT INTO `s_events` VALUES ('24', '4', '1', '2018-11-29 20:04:54', '5', null, null, '0', '0', '0', null, '        有的人你看了一辈子却忽视了一辈子，有的人你只看了一眼却影响了你的一生，有的人热情的为你而快乐却被你冷落，有的人让你拥有短暂的快乐却得到你思绪的连锁，有的人一相情愿了N年却被你拒绝了N年，有的人一个无心的表情却成了永恒的思念，这就是人生。<img class=\"emojione\" alt=\"\" src=\"http://www.share.com/img/emoji/1F614.png?v=1.2.4\"/><img class=\"emojione\" alt=\"\" src=\"http://www.share.com/img/emoji/1F614.png?v=1.2.4\"/><img class=\"emojione\" alt=\"\" src=\"http://www.share.com/img/emoji/1F614.png?v=1.2.4\"/>', null, null, '0', null, '0', null);
INSERT INTO `s_events` VALUES ('25', '0', '1', '2018-11-29 20:07:03', '4', null, null, '0', '0', '0', '今天的心情', '今天做邮箱验证的时候，遇到一个比较奇怪的问题： javax.mail.SendFailedException: Invalid Addresses;   nested exception is: com.sun.mail.smtp.SMTPAddressFailedException: 550 Invalid User: \"13540619068@163.com\" at com.sun.mail.', null, null, '0', null, '0', null);
INSERT INTO `s_events` VALUES ('26', '2', '7', '2018-11-29 20:14:47', '5', null, null, '0', '0', '0', '/upload/2/c/9/0/4/a/4/0/642fc684-76d1-49a9-b412-e735338aa261.jpg', '#余生', '/upload/2/c/9/0/4/a/4/0/642fc684-76d1-49a9-b412-e735338aa261.jpg:', null, '0', null, '0', null);
INSERT INTO `s_events` VALUES ('27', '0', '1', '2018-11-29 20:19:42', '5', null, null, '0', '0', '0', '鑫宇你看到了吗？', ' 以后呀，我们要是有幸在一起了，我们家务活一定要分工明确，你收拾屋子，我收拾你。', '/upload/2/f/3/4/b/2/7/0/e15e2b01-1610-4e85-be8c-f04b6cdf8261.jpg', null, '0', null, '0', null);
INSERT INTO `s_events` VALUES ('28', '0', '1', '2018-11-29 20:25:16', '5', null, null, '0', '0', '0', '你怎么还没看见呢？哼╭(╯^╰)╮', '懂事的人一旦不配合就会被说没良心，任性的人稍微乖巧一回就被夸个不停。', '/upload/2/d/7/1/c/2/5/0/7dc4af3d-6898-4c7f-8c7f-aebb25e02657.jpg', null, '0', null, '0', null);
INSERT INTO `s_events` VALUES ('29', '4', '1', '2018-11-29 20:27:38', '6', null, null, '0', '0', '0', null, '今年湖人能夺冠么？\n', null, null, '0', null, '0', null);
INSERT INTO `s_events` VALUES ('30', '0', '1', '2018-11-29 20:27:39', '5', null, null, '0', '0', '0', '我真的生气了！哄不好的那种╭(╯^╰)╮', ' 遇见的人多了，你才会明白哪些人值得用生命去珍惜，而一些人只适合绕道而行。', '/upload/3/2/b/a/9/2/b/0/fc2883b6-3d1b-4e85-bfec-715f6727b561.jpg', null, '0', null, '0', null);
INSERT INTO `s_events` VALUES ('31', '4', '1', '2018-11-29 20:27:42', '6', null, null, '0', '0', '0', null, '<img class=\"emojione\" alt=\"\" src=\"http://www.share.com/img/emoji/1F600.png?v=1.2.4\"/><img class=\"emojione\" alt=\"\" src=\"http://www.share.com/img/emoji/1F600.png?v=1.2.4\"/><img class=\"emojione\" alt=\"\" src=\"http://www.share.com/img/emoji/1F600.png?v=1.2.4\"/><img class=\"emojione\" alt=\"\" src=\"http://www.share.com/img/emoji/1F600.png?v=1.2.4\"/>', null, null, '0', null, '0', null);
INSERT INTO `s_events` VALUES ('32', '0', '1', '2018-11-29 20:30:06', '5', null, null, '0', '0', '0', '大坏蛋！！！！！！！！！！', ' 很多时候，碍于面子，你不说，我也不说，很多事情就黄了；很多时候，为了形象，该说的不直说，很多关系就凉了！', '/upload/2/e/5/3/3/a/6/0/8b395621-8bdf-4458-85f9-3a831e349902.jpg', null, '0', null, '0', null);
INSERT INTO `s_events` VALUES ('33', '0', '1', '2018-11-29 20:33:41', '5', null, null, '0', '0', '0', '你有本事一辈子都不要理我@.@', '傻是我的特长，痴是我的理想，当傻和痴交织在一起的时候，便是我梦境里最美的天堂！别笑我，我就这么痴心，我会傻傻地爱你痴痴地恋你，一直到老。', '/upload/3/0/f/7/a/2/9/0/24650727-c2b2-4471-8d51-e20ca435403c.jpg', null, '0', null, '0', null);
INSERT INTO `s_events` VALUES ('34', '0', '1', '2018-11-29 20:36:10', '5', null, null, '0', '0', '0', '我好难过~', '不要对一个人太好，因为你终于有一天会发现，对一个人好，时间久了，那个人是会习惯的，然后把这一切看作是理所应当，其实本来是可以蠢到不计代价不顾回报的，但现实总是让人寒了心。其实你明明知道，最卑贱不过感情，最凉不过是人心……', '/upload/3/0/1/6/2/a/8/0/9319b393-8b97-441e-b76f-be9a8313d222.jpg', null, '0', null, '0', null);
INSERT INTO `s_events` VALUES ('35', '0', '1', '2018-11-29 20:39:11', '5', null, null, '0', '0', '0', '人生呐~哎呀。。。哭哭，，', ' 有些人莫名其妙的闯进你的世界，给了你想要的温暖和陪伴，然而又莫名其妙的消失了。', '/upload/3/1/d/9/1/a/a/0/688529a2-0948-4de3-bc8c-df4f7e787607.jpg', null, '0', null, '0', null);

-- ----------------------------
-- Table structure for `s_followers`
-- ----------------------------
DROP TABLE IF EXISTS `s_followers`;
CREATE TABLE `s_followers` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `user_name` varchar(100) DEFAULT NULL,
  `follower_user_id` int(11) NOT NULL,
  `follower_user_name` varchar(100) DEFAULT NULL,
  `ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`,`follower_user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of s_followers
-- ----------------------------
INSERT INTO `s_followers` VALUES ('1', '1', 'fxy', '2', null, '2018-11-29 17:50:00');
INSERT INTO `s_followers` VALUES ('2', '3', '超', '5', null, '2018-11-29 19:33:12');
INSERT INTO `s_followers` VALUES ('3', '1', 'fxy', '3', null, '2018-11-29 19:48:59');
INSERT INTO `s_followers` VALUES ('4', '4', '李飒', '3', null, '2018-11-29 19:49:02');
INSERT INTO `s_followers` VALUES ('5', '3', '超', '6', null, '2018-11-29 19:57:07');
INSERT INTO `s_followers` VALUES ('6', '1', 'fxy', '6', null, '2018-11-29 19:57:13');
INSERT INTO `s_followers` VALUES ('7', '2', '智障', '6', null, '2018-11-29 19:57:17');
INSERT INTO `s_followers` VALUES ('8', '4', '李飒', '6', null, '2018-11-29 19:57:21');
INSERT INTO `s_followers` VALUES ('9', '3', '超', '4', null, '2018-11-29 19:58:47');
INSERT INTO `s_followers` VALUES ('10', '4', '李飒', '2', null, '2018-11-29 20:02:21');
INSERT INTO `s_followers` VALUES ('11', '3', '超', '2', null, '2018-11-29 20:02:35');
INSERT INTO `s_followers` VALUES ('12', '2', '郭超的爸爸', '5', null, '2018-11-29 20:03:20');
INSERT INTO `s_followers` VALUES ('13', '4', '李飒', '7', null, '2018-11-29 20:03:45');
INSERT INTO `s_followers` VALUES ('25', '2', '垃圾', '4', null, '2018-11-29 20:21:56');
INSERT INTO `s_followers` VALUES ('26', '5', '于洁', '4', null, '2018-11-29 20:22:47');
INSERT INTO `s_followers` VALUES ('27', '4', '李飒', '5', null, '2018-11-29 20:22:49');
INSERT INTO `s_followers` VALUES ('28', '3', '超', '8', null, '2018-11-29 20:29:47');
INSERT INTO `s_followers` VALUES ('29', '1', 'fxy', '5', null, '2018-11-29 20:31:09');

-- ----------------------------
-- Table structure for `s_followings`
-- ----------------------------
DROP TABLE IF EXISTS `s_followings`;
CREATE TABLE `s_followings` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `user_name` varchar(100) DEFAULT NULL,
  `following_user_id` int(11) NOT NULL,
  `following_user_name` varchar(100) DEFAULT NULL,
  `ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`,`following_user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of s_followings
-- ----------------------------
INSERT INTO `s_followings` VALUES ('1', '2', '郭超的爸爸', '1', 'fxy', '2018-11-29 17:50:00');
INSERT INTO `s_followings` VALUES ('2', '5', '杰杰', '3', '超', '2018-11-29 19:33:12');
INSERT INTO `s_followings` VALUES ('3', '3', '超', '1', 'fxy', '2018-11-29 19:48:59');
INSERT INTO `s_followings` VALUES ('4', '3', '超', '4', '李飒', '2018-11-29 19:49:02');
INSERT INTO `s_followings` VALUES ('5', '6', '郭美丽', '3', '超', '2018-11-29 19:57:07');
INSERT INTO `s_followings` VALUES ('6', '6', '郭美丽', '1', 'fxy', '2018-11-29 19:57:13');
INSERT INTO `s_followings` VALUES ('7', '6', '郭美丽', '2', '智障', '2018-11-29 19:57:17');
INSERT INTO `s_followings` VALUES ('8', '6', '郭美丽', '4', '李飒', '2018-11-29 19:57:21');
INSERT INTO `s_followings` VALUES ('9', '4', '李飒', '3', '超', '2018-11-29 19:58:47');
INSERT INTO `s_followings` VALUES ('10', '2', '垃圾', '4', '李飒', '2018-11-29 20:02:21');
INSERT INTO `s_followings` VALUES ('11', '2', '郭超的爸爸', '3', '超', '2018-11-29 20:02:35');
INSERT INTO `s_followings` VALUES ('12', '5', '于洁', '2', '郭超的爸爸', '2018-11-29 20:03:20');
INSERT INTO `s_followings` VALUES ('13', '7', 'wj', '4', '李飒', '2018-11-29 20:03:45');
INSERT INTO `s_followings` VALUES ('39', '4', '李飒', '2', '垃圾', '2018-11-29 20:21:56');
INSERT INTO `s_followings` VALUES ('40', '4', '李飒', '5', '于洁', '2018-11-29 20:22:47');
INSERT INTO `s_followings` VALUES ('41', '5', '于洁', '4', '李飒', '2018-11-29 20:22:49');
INSERT INTO `s_followings` VALUES ('42', '8', '周猴子', '3', '超', '2018-11-29 20:29:47');
INSERT INTO `s_followings` VALUES ('43', '5', '于洁', '1', 'fxy', '2018-11-29 20:31:09');

-- ----------------------------
-- Table structure for `s_interests`
-- ----------------------------
DROP TABLE IF EXISTS `s_interests`;
CREATE TABLE `s_interests` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `tag_id` int(11) NOT NULL,
  `ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`,`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of s_interests
-- ----------------------------

-- ----------------------------
-- Table structure for `s_likes`
-- ----------------------------
DROP TABLE IF EXISTS `s_likes`;
CREATE TABLE `s_likes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `object_type` int(11) NOT NULL,
  `object_id` int(11) NOT NULL,
  `ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`,`object_type`,`object_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of s_likes
-- ----------------------------
INSERT INTO `s_likes` VALUES ('6', '3', '2', '2', '2018-11-29 18:42:11');
INSERT INTO `s_likes` VALUES ('7', '3', '4', '1', '2018-11-29 18:42:14');
INSERT INTO `s_likes` VALUES ('8', '5', '2', '2', '2018-11-29 19:34:48');
INSERT INTO `s_likes` VALUES ('9', '3', '2', '3', '2018-11-29 19:48:40');
INSERT INTO `s_likes` VALUES ('11', '6', '2', '4', '2018-11-29 19:58:38');
INSERT INTO `s_likes` VALUES ('13', '4', '4', '1', '2018-11-29 20:01:00');
INSERT INTO `s_likes` VALUES ('14', '5', '0', '1', '2018-11-29 20:26:02');

-- ----------------------------
-- Table structure for `s_notifications`
-- ----------------------------
DROP TABLE IF EXISTS `s_notifications`;
CREATE TABLE `s_notifications` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `notify_type` int(11) NOT NULL,
  `notify_id` int(11) NOT NULL,
  `object_type` int(11) NOT NULL,
  `object_id` int(11) NOT NULL,
  `notified_user` int(11) NOT NULL,
  `notifier` int(11) NOT NULL,
  `ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of s_notifications
-- ----------------------------
INSERT INTO `s_notifications` VALUES ('1', '4', '0', '5', '1', '1', '2', '2018-11-29 17:50:00', '0');
INSERT INTO `s_notifications` VALUES ('2', '3', '0', '2', '2', '3', '3', '2018-11-29 18:41:54', '0');
INSERT INTO `s_notifications` VALUES ('3', '3', '0', '4', '1', '3', '3', '2018-11-29 18:41:55', '0');
INSERT INTO `s_notifications` VALUES ('4', '3', '0', '4', '1', '3', '3', '2018-11-29 18:42:02', '0');
INSERT INTO `s_notifications` VALUES ('5', '3', '0', '2', '2', '3', '3', '2018-11-29 18:42:06', '0');
INSERT INTO `s_notifications` VALUES ('6', '3', '0', '4', '1', '3', '3', '2018-11-29 18:42:08', '0');
INSERT INTO `s_notifications` VALUES ('7', '3', '0', '2', '2', '3', '3', '2018-11-29 18:42:11', '0');
INSERT INTO `s_notifications` VALUES ('8', '3', '0', '4', '1', '3', '3', '2018-11-29 18:42:14', '0');
INSERT INTO `s_notifications` VALUES ('9', '1', '1', '2', '2', '3', '3', '2018-11-29 18:42:25', '0');
INSERT INTO `s_notifications` VALUES ('10', '4', '0', '5', '3', '3', '5', '2018-11-29 19:33:13', '0');
INSERT INTO `s_notifications` VALUES ('11', '1', '1', '1', '2', '3', '5', '2018-11-29 19:33:31', '0');
INSERT INTO `s_notifications` VALUES ('12', '3', '0', '2', '2', '3', '5', '2018-11-29 19:34:48', '0');
INSERT INTO `s_notifications` VALUES ('13', '3', '0', '2', '3', '3', '3', '2018-11-29 19:48:40', '0');
INSERT INTO `s_notifications` VALUES ('14', '3', '0', '2', '3', '3', '3', '2018-11-29 19:48:42', '0');
INSERT INTO `s_notifications` VALUES ('15', '4', '0', '5', '1', '1', '3', '2018-11-29 19:48:59', '0');
INSERT INTO `s_notifications` VALUES ('16', '4', '0', '5', '4', '4', '3', '2018-11-29 19:49:02', '0');
INSERT INTO `s_notifications` VALUES ('17', '4', '0', '5', '3', '3', '6', '2018-11-29 19:57:07', '0');
INSERT INTO `s_notifications` VALUES ('18', '4', '0', '5', '1', '1', '6', '2018-11-29 19:57:14', '0');
INSERT INTO `s_notifications` VALUES ('19', '4', '0', '5', '2', '2', '6', '2018-11-29 19:57:17', '0');
INSERT INTO `s_notifications` VALUES ('20', '4', '0', '5', '4', '4', '6', '2018-11-29 19:57:21', '0');
INSERT INTO `s_notifications` VALUES ('21', '3', '0', '2', '4', '6', '6', '2018-11-29 19:58:38', '0');
INSERT INTO `s_notifications` VALUES ('22', '4', '0', '5', '3', '3', '4', '2018-11-29 19:58:47', '0');
INSERT INTO `s_notifications` VALUES ('23', '3', '0', '0', '1', '3', '4', '2018-11-29 19:59:44', '0');
INSERT INTO `s_notifications` VALUES ('24', '3', '0', '4', '1', '3', '4', '2018-11-29 20:01:00', '0');
INSERT INTO `s_notifications` VALUES ('25', '4', '0', '5', '4', '4', '2', '2018-11-29 20:02:21', '0');
INSERT INTO `s_notifications` VALUES ('26', '4', '0', '5', '3', '3', '2', '2018-11-29 20:02:35', '0');
INSERT INTO `s_notifications` VALUES ('27', '4', '0', '5', '2', '2', '5', '2018-11-29 20:03:20', '0');
INSERT INTO `s_notifications` VALUES ('28', '4', '0', '5', '4', '4', '7', '2018-11-29 20:03:45', '0');
INSERT INTO `s_notifications` VALUES ('29', '4', '0', '5', '4', '4', '3', '2018-11-29 20:04:26', '0');
INSERT INTO `s_notifications` VALUES ('30', '4', '0', '5', '4', '4', '3', '2018-11-29 20:04:28', '0');
INSERT INTO `s_notifications` VALUES ('31', '4', '0', '5', '4', '4', '3', '2018-11-29 20:04:29', '0');
INSERT INTO `s_notifications` VALUES ('32', '4', '0', '5', '4', '4', '3', '2018-11-29 20:04:29', '0');
INSERT INTO `s_notifications` VALUES ('33', '4', '0', '5', '4', '4', '3', '2018-11-29 20:04:29', '0');
INSERT INTO `s_notifications` VALUES ('34', '4', '0', '5', '4', '4', '3', '2018-11-29 20:04:29', '0');
INSERT INTO `s_notifications` VALUES ('35', '4', '0', '5', '4', '4', '3', '2018-11-29 20:04:30', '0');
INSERT INTO `s_notifications` VALUES ('36', '4', '0', '5', '4', '4', '3', '2018-11-29 20:04:30', '0');
INSERT INTO `s_notifications` VALUES ('37', '4', '0', '5', '1', '1', '3', '2018-11-29 20:04:31', '0');
INSERT INTO `s_notifications` VALUES ('38', '4', '0', '5', '1', '1', '3', '2018-11-29 20:04:31', '0');
INSERT INTO `s_notifications` VALUES ('39', '4', '0', '5', '1', '1', '3', '2018-11-29 20:04:31', '0');
INSERT INTO `s_notifications` VALUES ('40', '4', '0', '5', '4', '4', '3', '2018-11-29 20:04:43', '0');
INSERT INTO `s_notifications` VALUES ('41', '4', '0', '5', '4', '4', '3', '2018-11-29 20:04:44', '0');
INSERT INTO `s_notifications` VALUES ('42', '4', '0', '5', '4', '4', '3', '2018-11-29 20:07:33', '0');
INSERT INTO `s_notifications` VALUES ('43', '4', '0', '5', '1', '1', '3', '2018-11-29 20:10:46', '0');
INSERT INTO `s_notifications` VALUES ('44', '4', '0', '5', '4', '4', '3', '2018-11-29 20:10:51', '0');
INSERT INTO `s_notifications` VALUES ('45', '4', '0', '5', '4', '4', '3', '2018-11-29 20:10:51', '0');
INSERT INTO `s_notifications` VALUES ('46', '4', '0', '5', '4', '4', '3', '2018-11-29 20:10:52', '0');
INSERT INTO `s_notifications` VALUES ('47', '4', '0', '5', '4', '4', '3', '2018-11-29 20:10:52', '0');
INSERT INTO `s_notifications` VALUES ('48', '4', '0', '5', '4', '4', '3', '2018-11-29 20:10:52', '0');
INSERT INTO `s_notifications` VALUES ('49', '4', '0', '5', '4', '4', '3', '2018-11-29 20:10:52', '0');
INSERT INTO `s_notifications` VALUES ('50', '4', '0', '5', '4', '4', '3', '2018-11-29 20:10:52', '0');
INSERT INTO `s_notifications` VALUES ('51', '4', '0', '5', '4', '4', '3', '2018-11-29 20:10:54', '0');
INSERT INTO `s_notifications` VALUES ('52', '4', '0', '5', '4', '4', '3', '2018-11-29 20:10:54', '0');
INSERT INTO `s_notifications` VALUES ('53', '4', '0', '5', '4', '4', '3', '2018-11-29 20:10:54', '0');
INSERT INTO `s_notifications` VALUES ('54', '4', '0', '5', '2', '2', '4', '2018-11-29 20:21:56', '0');
INSERT INTO `s_notifications` VALUES ('55', '1', '1', '0', '1', '2', '4', '2018-11-29 20:22:06', '0');
INSERT INTO `s_notifications` VALUES ('56', '4', '0', '5', '5', '5', '4', '2018-11-29 20:22:47', '0');
INSERT INTO `s_notifications` VALUES ('57', '4', '0', '5', '4', '4', '5', '2018-11-29 20:22:49', '0');
INSERT INTO `s_notifications` VALUES ('58', '3', '0', '0', '1', '5', '5', '2018-11-29 20:26:02', '0');
INSERT INTO `s_notifications` VALUES ('59', '2', '1', '0', '1', '4', '5', '2018-11-29 20:26:15', '0');
INSERT INTO `s_notifications` VALUES ('60', '4', '0', '5', '3', '3', '8', '2018-11-29 20:29:47', '0');
INSERT INTO `s_notifications` VALUES ('61', '4', '0', '5', '1', '1', '5', '2018-11-29 20:31:09', '0');

-- ----------------------------
-- Table structure for `s_photos`
-- ----------------------------
DROP TABLE IF EXISTS `s_photos`;
CREATE TABLE `s_photos` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `key` varchar(200) NOT NULL,
  `album_id` int(11) NOT NULL,
  `ts` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `desc` varchar(300) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of s_photos
-- ----------------------------
INSERT INTO `s_photos` VALUES ('1', '/upload/c/b/6/1/0/e/7/b/07f2c27e-58b9-45a0-9a23-7467722fae8c.jpg', '1', '2018-11-29 17:50:29', null);
INSERT INTO `s_photos` VALUES ('2', '/upload/4/4/1/7/2/e/8/f/8c976115-4796-4fd2-971b-4a4ece98ad25.jpg', '2', '2018-11-29 18:39:47', null);
INSERT INTO `s_photos` VALUES ('3', '/upload/a/3/f/5/b/3/d/e/398df2d0-8394-4957-b403-81df02055db3.jpg', '3', '2018-11-29 19:47:31', null);
INSERT INTO `s_photos` VALUES ('4', '/upload/1/5/a/d/c/3/f/f/a2c16d46-5789-4d0d-8818-5e3d8db35bb9.jpg', '3', '2018-11-29 19:47:36', null);
INSERT INTO `s_photos` VALUES ('5', '/upload/e/2/a/d/b/9/3/7/04b0c12d-d23b-40d3-b11a-d45136dc1fac.jpg', '3', '2018-11-29 19:47:54', null);
INSERT INTO `s_photos` VALUES ('6', '/upload/4/8/1/5/5/e/b/a/5f350fc2-f9ba-465d-9333-9f5a99140284.jpg', '4', '2018-11-29 19:57:47', null);
INSERT INTO `s_photos` VALUES ('7', '/upload/c/b/6/1/0/e/7/b/3f48febf-cd89-45e7-b9cf-1997a81b7535.jpg', '5', '2018-11-29 19:58:28', null);
INSERT INTO `s_photos` VALUES ('8', '/upload/e/3/7/d/c/1/8/d/91bb063a-7e55-435a-a466-5009cf94b31c.jpg', '4', '2018-11-29 20:01:52', null);
INSERT INTO `s_photos` VALUES ('9', '/upload/b/4/8/0/0/2/a/8/4b3a957a-e8ce-464b-aa63-217a64ffe1da.jpg', '5', '2018-11-29 20:02:21', null);
INSERT INTO `s_photos` VALUES ('10', '/upload/c/e/f/9/4/9/0/9/ccc64b70-3ca3-4acf-b1de-5d235b01a9c8.jpg', '5', '2018-11-29 20:02:24', null);
INSERT INTO `s_photos` VALUES ('11', '/upload/2/c/9/0/4/a/4/0/c9f44ec9-1361-4d08-ad34-6ec52f989311.jpg', '6', '2018-11-29 20:10:50', null);
INSERT INTO `s_photos` VALUES ('12', '/upload/2/c/9/0/4/a/4/0/33e5477c-4fbd-482d-b1c0-5b2490f0b934.jpg', '7', '2018-11-29 20:13:50', null);
INSERT INTO `s_photos` VALUES ('13', '/upload/2/c/9/0/4/a/4/0/642fc684-76d1-49a9-b412-e735338aa261.jpg', '7', '2018-11-29 20:14:17', null);

-- ----------------------------
-- Table structure for `s_posts`
-- ----------------------------
DROP TABLE IF EXISTS `s_posts`;
CREATE TABLE `s_posts` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `post_author` int(11) DEFAULT NULL COMMENT '作者ID',
  `post_ts` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `post_content` longtext,
  `post_title` text,
  `post_excerpt` text COMMENT '摘要',
  `post_status` int(11) DEFAULT '0',
  `comment_status` int(11) DEFAULT '0',
  `post_pwd` varchar(100) DEFAULT NULL,
  `post_lastts` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `comment_count` int(11) DEFAULT '0',
  `like_count` int(11) DEFAULT '0',
  `share_count` int(11) DEFAULT '0',
  `post_url` varchar(45) DEFAULT NULL,
  `post_tags` text,
  `post_album` int(11) DEFAULT '0',
  `post_cover` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_osf_users_post_author_idx` (`post_author`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of s_posts
-- ----------------------------
INSERT INTO `s_posts` VALUES ('1', '2', '2018-11-29 17:49:45', '我是郭超的爸爸', null, null, '0', '0', null, '2018-11-29 17:49:45', '0', '0', '0', null, null, '0', null);
INSERT INTO `s_posts` VALUES ('2', '3', '2018-11-29 18:39:28', '周四晚。完成了，嗨皮<img class=\"emojione\" alt=\"\" src=\"http://www.share.com/img/emoji/1F600.png?v=1.2.4\"/><img class=\"emojione\" alt=\"\" src=\"http://www.share.com/img/emoji/1F600.png?v=1.2.4\"/><img class=\"emojione\" alt=\"\" src=\"http://www.share.com/img/emoji/1F600.png?v=1.2.4\"/><img class=\"emojione\" alt=\"\" src=\"http://www.share.com/img/emoji/1F600.png?v=1.2.4\"/><img class=\"emojione\" alt=\"\" src=\"http://www.share.com/img/emoji/1F600.png?v=1.2.4\"/><img class=\"emojione\" alt=\"\" src=\"http://www.share.com/img/emoji/1F600.png?v=1.2.4\"/>', null, null, '0', '0', null, '2018-11-29 18:39:28', '0', '0', '0', null, null, '0', null);
INSERT INTO `s_posts` VALUES ('3', '3', '2018-11-29 19:48:35', '<p>惨啊惨啊</p>', '库里车祸', '惨啊惨啊', '0', '0', null, '2018-11-29 19:48:35', '0', '0', '0', null, null, '0', null);
INSERT INTO `s_posts` VALUES ('4', '3', '2018-11-29 19:50:26', '从小生活在江西，长大离开家后，格外想念家乡的那片红土地', null, null, '0', '0', null, '2018-11-29 19:50:26', '0', '0', '0', null, null, '0', null);
INSERT INTO `s_posts` VALUES ('5', '3', '2018-11-29 19:50:43', '【因老伴生前一句话，他坚持5年抄完四大名著】5年前，江苏南京老人王振顺的老伴因心梗突然去世，生前曾嘱托他以后多写点字。于是，5年来，身患帕金森氏综合征的王振顺即使眼睛模糊了，笔也拿不稳了，仍坚持每天写字。328万字、3815张宣纸……今年10月，他抄完四大名著：老伴布置的作业，一定要完成', null, null, '0', '0', null, '2018-11-29 19:50:43', '0', '0', '0', null, null, '0', null);
INSERT INTO `s_posts` VALUES ('6', '3', '2018-11-29 19:50:52', '央视新闻”微博是中央电视台新闻中心官方微博，是央视重大新闻、突发事件、重点报道的首发平台。<img class=\"emojione\" alt=\"\" src=\"http://www.share.com/img/emoji/1F604.png?v=1.2.4\"/>', null, null, '0', '0', null, '2018-11-29 19:50:52', '0', '0', '0', null, null, '0', null);
INSERT INTO `s_posts` VALUES ('7', '3', '2018-11-29 19:51:04', '258人违规、21人永久禁赛！深圳南山半马大量参赛者作弊被罚<img class=\"emojione\" alt=\"\" src=\"http://www.share.com/img/emoji/1F603.png?v=1.2.4\"/><img class=\"emojione\" alt=\"\" src=\"http://www.share.com/img/emoji/1F603.png?v=1.2.4\"/><img class=\"emojione\" alt=\"\" src=\"http://www.share.com/img/emoji/1F603.png?v=1.2.4\"/><img class=\"emojione\" alt=\"\" src=\"http://www.share.com/img/emoji/1F603.png?v=1.2.4\"/>', null, null, '0', '0', null, '2018-11-29 19:51:04', '0', '0', '0', null, null, '0', null);
INSERT INTO `s_posts` VALUES ('8', '3', '2018-11-29 19:51:42', '<p align=\"center\" style=\"margin: 0px 0px 16px; padding: 0px; color: rgb(51, 51, 51); font-family: Arial, &quot;PingFang SC&quot;, &quot;Hiragino Sans GB&quot;, &quot;Microsoft YaHei&quot;, &quot;WenQuanYi Micro Hei&quot;, sans-serif; font-size: 16px; font-style: normal; font-variant-ligatures: normal; font-variant-caps: normal; font-weight: 400; letter-spacing: normal; orphans: 2; text-indent: 0px; text-transform: none; white-space: normal; widows: 2; word-spacing: 0px; -webkit-text-stroke-width: 0px; text-decoration-style: initial; text-decoration-color: initial;\">上周日深圳南山一场热热闹闹的半程马拉松赛<br></p><p align=\"center\" style=\"margin: 0px 0px 16px; padding: 0px; color: rgb(51, 51, 51); font-family: Arial, &quot;PingFang SC&quot;, &quot;Hiragino Sans GB&quot;, &quot;Microsoft YaHei&quot;, &quot;WenQuanYi Micro Hei&quot;, sans-serif; font-size: 16px; font-style: normal; font-variant-ligatures: normal; font-variant-caps: normal; font-weight: 400; letter-spacing: normal; orphans: 2; text-indent: 0px; text-transform: none; white-space: normal; widows: 2; word-spacing: 0px; -webkit-text-stroke-width: 0px; text-decoration-style: initial; text-decoration-color: initial;\">成了这几天令人“尴尬”的话题</p><p align=\"center\" style=\"margin: 0px 0px 16px; padding: 0px; color: rgb(51, 51, 51); font-family: Arial, &quot;PingFang SC&quot;, &quot;Hiragino Sans GB&quot;, &quot;Microsoft YaHei&quot;, &quot;WenQuanYi Micro Hei&quot;, sans-serif; font-size: 16px; font-style: normal; font-variant-ligatures: normal; font-variant-caps: normal; font-weight: 400; letter-spacing: normal; orphans: 2; text-indent: 0px; text-transform: none; white-space: normal; widows: 2; word-spacing: 0px; -webkit-text-stroke-width: 0px; text-decoration-style: initial; text-decoration-color: initial;\">南山半马被曝集体抄近路作弊、大面积套牌</p><p align=\"center\" style=\"margin: 0px 0px 16px; padding: 0px; color: rgb(51, 51, 51); font-family: Arial, &quot;PingFang SC&quot;, &quot;Hiragino Sans GB&quot;, &quot;Microsoft YaHei&quot;, &quot;WenQuanYi Micro Hei&quot;, sans-serif; font-size: 16px; font-style: normal; font-variant-ligatures: normal; font-variant-caps: normal; font-weight: 400; letter-spacing: normal; orphans: 2; text-indent: 0px; text-transform: none; white-space: normal; widows: 2; word-spacing: 0px; -webkit-text-stroke-width: 0px; text-decoration-style: initial; text-decoration-color: initial;\"><br></p><h2 style=\"margin: 0px 0px 16px; padding: 0px; font-size: 22px; font-weight: normal; color: rgb(51, 51, 51); font-family: Arial, &quot;PingFang SC&quot;, &quot;Hiragino Sans GB&quot;, &quot;Microsoft YaHei&quot;, &quot;WenQuanYi Micro Hei&quot;, sans-serif; font-style: normal; font-variant-ligatures: normal; font-variant-caps: normal; letter-spacing: normal; orphans: 2; text-align: start; text-indent: 0px; text-transform: none; white-space: normal; widows: 2; word-spacing: 0px; -webkit-text-stroke-width: 0px; text-decoration-style: initial; text-decoration-color: initial;\">赛事组委会发布处理意见公告</h2><p align=\"justify\" style=\"margin: 0px 0px 16px; padding: 0px; color: rgb(51, 51, 51); font-family: Arial, &quot;PingFang SC&quot;, &quot;Hiragino Sans GB&quot;, &quot;Microsoft YaHei&quot;, &quot;WenQuanYi Micro Hei&quot;, sans-serif; font-size: 16px; font-style: normal; font-variant-ligatures: normal; font-variant-caps: normal; font-weight: 400; letter-spacing: normal; orphans: 2; text-indent: 0px; text-transform: none; white-space: normal; widows: 2; word-spacing: 0px; -webkit-text-stroke-width: 0px; text-decoration-style: initial; text-decoration-color: initial;\">28日，赛事组委会发布对违规选手处理意见的公告，根据计时芯片数据、赛会录像、现场照片等材料证明，<b>总计违规选手人数258人，其中伪造号码布18人，替跑3人</b>。</p><p align=\"justify\" style=\"margin: 0px 0px 16px; padding: 0px; color: rgb(51, 51, 51); font-family: Arial, &quot;PingFang SC&quot;, &quot;Hiragino Sans GB&quot;, &quot;Microsoft YaHei&quot;, &quot;WenQuanYi Micro Hei&quot;, sans-serif; font-size: 16px; font-style: normal; font-variant-ligatures: normal; font-variant-caps: normal; font-weight: 400; letter-spacing: normal; orphans: 2; text-indent: 0px; text-transform: none; white-space: normal; widows: 2; word-spacing: 0px; -webkit-text-stroke-width: 0px; text-decoration-style: initial; text-decoration-color: initial;\">按照《2018深圳南山半程马拉松竞赛规程》第7-8条规定：</p><p align=\"justify\" style=\"margin: 0px 0px 16px; padding: 0px; color: rgb(51, 51, 51); font-family: Arial, &quot;PingFang SC&quot;, &quot;Hiragino Sans GB&quot;, &quot;Microsoft YaHei&quot;, &quot;WenQuanYi Micro Hei&quot;, sans-serif; font-size: 16px; font-style: normal; font-variant-ligatures: normal; font-variant-caps: normal; font-weight: 400; letter-spacing: normal; orphans: 2; text-indent: 0px; text-transform: none; white-space: normal; widows: 2; word-spacing: 0px; -webkit-text-stroke-width: 0px; text-decoration-style: initial; text-decoration-color: initial;\">伪造号码布及替跑选手将被永久禁止参加深圳南山半程马拉松。</p><p align=\"justify\" style=\"margin: 0px 0px 16px; padding: 0px; color: rgb(51, 51, 51); font-family: Arial, &quot;PingFang SC&quot;, &quot;Hiragino Sans GB&quot;, &quot;Microsoft YaHei&quot;, &quot;WenQuanYi Micro Hei&quot;, sans-serif; font-size: 16px; font-style: normal; font-variant-ligatures: normal; font-variant-caps: normal; font-weight: 400; letter-spacing: normal; orphans: 2; text-indent: 0px; text-transform: none; white-space: normal; widows: 2; word-spacing: 0px; -webkit-text-stroke-width: 0px; text-decoration-style: initial; text-decoration-color: initial;\">而其他违规行为选手（包含穿越绿化带）两年内将被禁止参加深圳南山半程马拉松，并根据要求将违规名单及处理结果上报至中国田径协会。</p><h2 style=\"margin: 0px 0px 16px; padding: 0px; font-size: 22px; font-weight: normal; color: rgb(51, 51, 51); font-family: Arial, &quot;PingFang SC&quot;, &quot;Hiragino Sans GB&quot;, &quot;Microsoft YaHei&quot;, &quot;WenQuanYi Micro Hei&quot;, sans-serif; font-style: normal; font-variant-ligatures: normal; font-variant-caps: normal; letter-spacing: normal; orphans: 2; text-align: start; text-indent: 0px; text-transform: none; white-space: normal; widows: 2; word-spacing: 0px; -webkit-text-stroke-width: 0px; text-decoration-style: initial; text-decoration-color: initial;\">比赛出现多名套牌跑者 分不清谁真谁假</h2><p align=\"justify\" style=\"margin: 0px 0px 16px; padding: 0px; color: rgb(51, 51, 51); font-family: Arial, &quot;PingFang SC&quot;, &quot;Hiragino Sans GB&quot;, &quot;Microsoft YaHei&quot;, &quot;WenQuanYi Micro Hei&quot;, sans-serif; font-size: 16px; font-style: normal; font-variant-ligatures: normal; font-variant-caps: normal; font-weight: 400; letter-spacing: normal; orphans: 2; text-indent: 0px; text-transform: none; white-space: normal; widows: 2; word-spacing: 0px; -webkit-text-stroke-width: 0px; text-decoration-style: initial; text-decoration-color: initial;\">作为国内口碑较好的半程马拉松赛事，今年南山半马报名比去年还要火爆。今年个人共有38633名跑者报名，首日报名人数便超过3万，与去年总报名人数持平，但只能抽取12324名，中签率只有31.9%。而团体报名的中签率不到20%。</p><p align=\"justify\" style=\"margin: 0px 0px 16px; padding: 0px; color: rgb(51, 51, 51); font-family: Arial, &quot;PingFang SC&quot;, &quot;Hiragino Sans GB&quot;, &quot;Microsoft YaHei&quot;, &quot;WenQuanYi Micro Hei&quot;, sans-serif; font-size: 16px; font-style: normal; font-variant-ligatures: normal; font-variant-caps: normal; font-weight: 400; letter-spacing: normal; orphans: 2; text-indent: 0px; text-transform: none; white-space: normal; widows: 2; word-spacing: 0px; -webkit-text-stroke-width: 0px; text-decoration-style: initial; text-decoration-color: initial;\">或许是因为中签太难，在比赛中，出现了不少套牌现象：</p><p align=\"center\" style=\"margin: 0px 0px 16px; padding: 0px; color: rgb(51, 51, 51); font-family: Arial, &quot;PingFang SC&quot;, &quot;Hiragino Sans GB&quot;, &quot;Microsoft YaHei&quot;, &quot;WenQuanYi Micro Hei&quot;, sans-serif; font-size: 16px; font-style: normal; font-variant-ligatures: normal; font-variant-caps: normal; font-weight: 400; letter-spacing: normal; orphans: 2; text-indent: 0px; text-transform: none; white-space: normal; widows: 2; word-spacing: 0px; -webkit-text-stroke-width: 0px; text-decoration-style: initial; text-decoration-color: initial;\">以下三位同为E50484的选手</p><p align=\"center\" style=\"margin: 0px 0px 16px; padding: 0px; color: rgb(51, 51, 51); font-family: Arial, &quot;PingFang SC&quot;, &quot;Hiragino Sans GB&quot;, &quot;Microsoft YaHei&quot;, &quot;WenQuanYi Micro Hei&quot;, sans-serif; font-size: 16px; font-style: normal; font-variant-ligatures: normal; font-variant-caps: normal; font-weight: 400; letter-spacing: normal; orphans: 2; text-indent: 0px; text-transform: none; white-space: normal; widows: 2; word-spacing: 0px; -webkit-text-stroke-width: 0px; text-decoration-style: initial; text-decoration-color: initial;\">而且还有男有女</p><p align=\"center\" style=\"margin: 0px 0px 16px; padding: 0px; color: rgb(51, 51, 51); font-family: Arial, &quot;PingFang SC&quot;, &quot;Hiragino Sans GB&quot;, &quot;Microsoft YaHei&quot;, &quot;WenQuanYi Micro Hei&quot;, sans-serif; font-size: 16px; font-style: normal; font-variant-ligatures: normal; font-variant-caps: normal; font-weight: 400; letter-spacing: normal; orphans: 2; text-indent: 0px; text-transform: none; white-space: normal; widows: 2; word-spacing: 0px; -webkit-text-stroke-width: 0px; text-decoration-style: initial; text-decoration-color: initial;\">究竟哪个是真的呢？</p><p align=\"center\" style=\"margin: 0px 0px 16px; padding: 0px; color: rgb(51, 51, 51); font-family: Arial, &quot;PingFang SC&quot;, &quot;Hiragino Sans GB&quot;, &quot;Microsoft YaHei&quot;, &quot;WenQuanYi Micro Hei&quot;, sans-serif; font-size: 16px; font-style: normal; font-variant-ligatures: normal; font-variant-caps: normal; font-weight: 400; letter-spacing: normal; orphans: 2; text-indent: 0px; text-transform: none; white-space: normal; widows: 2; word-spacing: 0px; -webkit-text-stroke-width: 0px; text-decoration-style: initial; text-decoration-color: initial;\">↓ ↓ ↓</p>', '258人违规、21人永久禁赛！深圳南山半马大量参赛者作弊被罚', '上周日深圳南山一场热热闹闹的半程马拉松赛 成了这几天令人“尴尬”的话题 南山半马被曝集体抄近路作弊、大面积套牌 赛事组委会发布处理意见公告 28日，赛事组委会发布对违规选手处理意见的公告，根据计时芯片数据、赛会录像、现场照片等材料证明，总计违规选手人数258人，其中伪造号码布18人，替跑3人。 按照《2018深圳南山半程马拉松竞赛规程》第7-8条规定： 伪造号码布及替跑选手将被永久禁止参加深圳南山', '0', '0', null, '2018-11-29 19:51:42', '0', '0', '0', null, null, '0', null);
INSERT INTO `s_posts` VALUES ('9', '6', '2018-11-29 19:56:51', '周五国安要夺冠！', null, null, '0', '0', null, '2018-11-29 19:56:51', '0', '0', '0', null, null, '0', null);
INSERT INTO `s_posts` VALUES ('10', '6', '2018-11-29 20:00:14', '<p>大家好，我叫郭美丽。。。</p>', '美丽的一天', '大家好，我叫郭美丽。。。', '0', '0', null, '2018-11-29 20:00:14', '0', '0', '0', null, null, '0', null);
INSERT INTO `s_posts` VALUES ('11', '7', '2018-11-29 20:00:25', '<p style=\"margin: 0px 0px 15px; padding: 0px; color: rgb(64, 64, 64); font-family: &quot;PingFang SC&quot;, &quot;Lantinghei SC&quot;, &quot;Helvetica Neue&quot;, Helvetica, Arial, &quot;Microsoft YaHei&quot;, å¾®è½¯é›…é»‘, STHeitiSC-Light, simsun, å®‹ä½“, &quot;WenQuanYi Zen Hei&quot;, &quot;WenQuanYi Micro Hei&quot;, sans-serif; font-size: 18px; font-variant-ligatures: normal; font-variant-caps: normal; letter-spacing: normal; orphans: 2; text-align: start; text-indent: 0px; text-transform: none; white-space: normal; widows: 2; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: rgb(255, 255, 255); text-decoration-style: initial; text-decoration-color: initial;\">新华社北京11月29日电　国家主席习近平同俄罗斯总统普京29日分别向中俄能源商务论坛致贺信。</p><p style=\"margin: 0px 0px 15px; padding: 0px; color: rgb(64, 64, 64); font-family: &quot;PingFang SC&quot;, &quot;Lantinghei SC&quot;, &quot;Helvetica Neue&quot;, Helvetica, Arial, &quot;Microsoft YaHei&quot;, å¾®è½¯é›…é»‘, STHeitiSC-Light, simsun, å®‹ä½“, &quot;WenQuanYi Zen Hei&quot;, &quot;WenQuanYi Micro Hei&quot;, sans-serif; font-size: 18px; font-style: normal; font-variant-ligatures: normal; font-variant-caps: normal; font-weight: 400; letter-spacing: normal; orphans: 2; text-align: start; text-indent: 0px; text-transform: none; white-space: normal; widows: 2; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: rgb(255, 255, 255); text-decoration-style: initial; text-decoration-color: initial;\">　　习近平在贺信中指出，当前，中俄全面战略协作伙伴关系保持高水平运行，各领域合作不断发展。近年来，两国能源合作取得了丰硕成果，为促进两国经济社会发展发挥了积极作用。中方愿同俄方一道，共同推动中俄能源合作再上新台阶。</p><p style=\"margin: 0px 0px 15px; padding: 0px; color: rgb(64, 64, 64); font-family: &quot;PingFang SC&quot;, &quot;Lantinghei SC&quot;, &quot;Helvetica Neue&quot;, Helvetica, Arial, &quot;Microsoft YaHei&quot;, å¾®è½¯é›…é»‘, STHeitiSC-Light, simsun, å®‹ä½“, &quot;WenQuanYi Zen Hei&quot;, &quot;WenQuanYi Micro Hei&quot;, sans-serif; font-size: 18px; font-style: normal; font-variant-ligatures: normal; font-variant-caps: normal; font-weight: 400; letter-spacing: normal; orphans: 2; text-align: start; text-indent: 0px; text-transform: none; white-space: normal; widows: 2; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: rgb(255, 255, 255); text-decoration-style: initial; text-decoration-color: initial;\">　　习近平强调，举办中俄能源商务论坛是我和普京总统达成的重要共识，旨在为双方企业搭建直接对话交流的平台，广泛寻找合作机遇，精准对接合作需求。希望与会嘉宾围绕“进一步深化中俄能源贸易、投资及金融的全方位合作”的主题，深入沟通、凝聚共识，巩固现有合作，挖掘新的合作机会，推动中俄能源合作取得更多成果，更好惠及两国人民。</p><p style=\"margin: 0px 0px 15px; padding: 0px; color: rgb(64, 64, 64); font-family: &quot;PingFang SC&quot;, &quot;Lantinghei SC&quot;, &quot;Helvetica Neue&quot;, Helvetica, Arial, &quot;Microsoft YaHei&quot;, å¾®è½¯é›…é»‘, STHeitiSC-Light, simsun, å®‹ä½“, &quot;WenQuanYi Zen Hei&quot;, &quot;WenQuanYi Micro Hei&quot;, sans-serif; font-size: 18px; font-style: normal; font-variant-ligatures: normal; font-variant-caps: normal; font-weight: 400; letter-spacing: normal; orphans: 2; text-align: start; text-indent: 0px; text-transform: none; white-space: normal; widows: 2; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: rgb(255, 255, 255); text-decoration-style: initial; text-decoration-color: initial;\">　　普京在贺信中表示，俄中全面战略协作伙伴关系正在稳步持续提升，能源合作作为俄中关系的重要组成部分，近年来取得长足发展。本次论坛议程丰富，内容充实，议题涵盖双方在油气、煤炭、电力、投资、科研和环保等领域合作，充分表明两国能源企业具有十分广泛的共同利益。相信来自两国政府部门、能源企业、金融机构和智库的参会嘉宾将开展内容丰富和具有建设性的对话，探索双方互利合作的新模式。</p>', '百度新闻', '新华社北京11月29日电　国家主席习近平同俄罗斯总统普京29日分别向中俄能源商务论坛致贺信。 　　习近平在贺信中指出，当前，中俄全面战略协作伙伴关系保持高水平运行，各领域合作不断发展。近年来，两国能源合作取得了丰硕成果，为促进两国经济社会发展发挥了积极作用。中方愿同俄方一道，共同推动中俄能源合作再上新台阶。 　　习近平强调，举办中俄能源商务论坛是我和普京总统达成的重要共识，旨在为双方企业搭建直接对', '0', '0', null, '2018-11-29 20:00:25', '0', '0', '0', null, null, '0', null);
INSERT INTO `s_posts` VALUES ('12', '7', '2018-11-29 20:00:37', '<img class=\"emojione\" alt=\"\" src=\"http://www.share.com/img/emoji/1F602.png?v=1.2.4\"/><img class=\"emojione\" alt=\"\" src=\"http://www.share.com/img/emoji/1F602.png?v=1.2.4\"/><img class=\"emojione\" alt=\"\" src=\"http://www.share.com/img/emoji/1F602.png?v=1.2.4\"/><img class=\"emojione\" alt=\"\" src=\"http://www.share.com/img/emoji/1F603.png?v=1.2.4\"/><img class=\"emojione\" alt=\"\" src=\"http://www.share.com/img/emoji/1F603.png?v=1.2.4\"/><img class=\"emojione\" alt=\"\" src=\"http://www.share.com/img/emoji/1F603.png?v=1.2.4\"/>', null, null, '0', '0', null, '2018-11-29 20:00:37', '0', '0', '0', null, null, '0', null);
INSERT INTO `s_posts` VALUES ('13', '7', '2018-11-29 20:02:10', '<p>中国<em>新闻</em>网是知名的中文<em>新闻</em>门户网站,也是全球互联网中文<em>新闻</em>资讯最重要的原创内容供应商之一。依托中新社遍布全球的采编网络,每天24小时面向广大网民和网络媒体</p><p>《今日头条》(www.toutiao.com)是一款基于数据挖掘的推荐引擎产品,它为用户推荐有价值的、个性化的信息,提供连接人与信息的新型服务,是国内移动互联网领域成长最快</p>', '今日头条', '中国新闻网是知名的中文新闻门户网站,也是全球互联网中文新闻资讯最重要的原创内容供应商之一。依托中新社遍布全球的采编网络,每天24小时面向广大网民和网络媒体 《今日头条》(www.toutiao.com)是一款基于数据挖掘的推荐引擎产品,它为用户推荐有价值的、个性化的信息,提供连接人与信息的新型服务,是国内移动互联网领域成长最快', '0', '0', null, '2018-11-29 20:02:10', '0', '0', '0', null, null, '0', null);
INSERT INTO `s_posts` VALUES ('14', '2', '2018-11-29 20:03:03', '终于完成了', null, null, '0', '0', null, '2018-11-29 20:03:03', '0', '0', '0', null, null, '0', null);
INSERT INTO `s_posts` VALUES ('15', '2', '2018-11-29 20:03:10', '<img class=\"emojione\" alt=\"\" src=\"http://www.share.com/img/emoji/1F600.png?v=1.2.4\"/><img class=\"emojione\" alt=\"\" src=\"http://www.share.com/img/emoji/1F601.png?v=1.2.4\"/><img class=\"emojione\" alt=\"\" src=\"http://www.share.com/img/emoji/1F601.png?v=1.2.4\"/><img class=\"emojione\" alt=\"\" src=\"http://www.share.com/img/emoji/1F601.png?v=1.2.4\"/><img class=\"emojione\" alt=\"\" src=\"http://www.share.com/img/emoji/1F601.png?v=1.2.4\"/><img class=\"emojione\" alt=\"\" src=\"http://www.share.com/img/emoji/1F601.png?v=1.2.4\"/>', null, null, '0', '0', null, '2018-11-29 20:03:10', '0', '0', '0', null, null, '0', null);
INSERT INTO `s_posts` VALUES ('16', '5', '2018-11-29 20:04:54', '        有的人你看了一辈子却忽视了一辈子，有的人你只看了一眼却影响了你的一生，有的人热情的为你而快乐却被你冷落，有的人让你拥有短暂的快乐却得到你思绪的连锁，有的人一相情愿了N年却被你拒绝了N年，有的人一个无心的表情却成了永恒的思念，这就是人生。<img class=\"emojione\" alt=\"\" src=\"http://www.share.com/img/emoji/1F614.png?v=1.2.4\"/><img class=\"emojione\" alt=\"\" src=\"http://www.share.com/img/emoji/1F614.png?v=1.2.4\"/><img class=\"emojione\" alt=\"\" src=\"http://www.share.com/img/emoji/1F614.png?v=1.2.4\"/>', null, null, '0', '0', null, '2018-11-29 20:04:54', '0', '0', '0', null, null, '0', null);
INSERT INTO `s_posts` VALUES ('17', '4', '2018-11-29 20:07:02', '<h2><strong>今天做邮箱验证的时候，遇到一个比较奇怪的问题：</strong></h2><h2><br></h2><h2><strong>javax.mail.SendFailedException: Invalid Addresses;</strong></h2><h2><strong>&nbsp; nested exception is:</strong></h2><h2><strong>com.sun.mail.smtp.SMTPAddressFailedException: 550 Invalid User: \"13540619068@163.com\"</strong></h2><h2><br></h2><h2><br></h2><h2><strong>at com.sun.mail.smtp.SMTPTransport.rcptTo(SMTPTransport.java:1835)</strong></h2><h2><strong>at com.sun.mail.smtp.SMTPTransport.sendMessage(SMTPTransport.java:1098)</strong></h2><h2><strong>at javax.mail.Transport.send0(Transport.java:195)</strong></h2><h2><strong>at javax.mail.Transport.send(Transport.java:124)</strong></h2><h2><strong>at com.tgb.tools.SendEmail.send(SendEmail.java:62)</strong></h2><h2><strong>at com.tgb.manager.RegisterValidateService.processregister(RegisterValidateService.java:54)</strong></h2><h2><strong>at com.tgb.web.RegisterController.load(RegisterController.java:30)</strong></h2><h2><strong>at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)</strong></h2><h2><strong>at sun.reflect.NativeMethodAccessorImpl.invoke(Unknown Source)</strong></h2><h2><strong>at sun.reflect.DelegatingMethodAccessorImpl.invoke(Unknown Source)</strong></h2><h2><strong>at java.lang.reflect.Method.invoke(Unknown Source)</strong></h2><h2><strong>at org.springframework.web.method.support.InvocableHandlerMethod.invoke(InvocableHandlerMethod.java:219)</strong></h2><h2><strong>at org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:132)</strong></h2><h2><strong>at org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:104)</strong></h2><h2><strong>at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandleMethod(RequestMappingHandlerAdapter.java:746)</strong></h2><h2><strong>at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:687)</strong></h2><h2><strong>at org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:80)</strong></h2><h2><strong>at org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:925)</strong></h2><h2><strong>at org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:856)</strong></h2><h2><strong>at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:915)</strong></h2><h2><strong>at org.springframework.web.servlet.FrameworkServlet.doGet(FrameworkServlet.java:811)</strong></h2><h2><strong>at javax.servlet.http.HttpServlet.service(HttpServlet.java:621)</strong></h2><h2><strong>at org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:796)</strong></h2><h2><strong>at javax.servlet.http.HttpServlet.service(HttpServlet.java:728)</strong></h2><h2><strong>at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:305)</strong></h2><h2><strong>at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:210)</strong></h2><h2><strong>at org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:52)</strong></h2><h2><strong>at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:243)</strong></h2><h2><strong>at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:210)</strong></h2><h2><strong>at org.springframework.orm.hibernate4.support.OpenSessionInViewFilter.doFilterInternal(OpenSessionInViewFilter.java:152)</strong></h2><h2><strong>at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107)</strong></h2><h2><strong>at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:243)</strong></h2><h2><strong>at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:210)</strong></h2><h2><strong>at org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:88)</strong></h2><h2><strong>at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107)</strong></h2><h2><strong>at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:243)</strong></h2><h2><strong>at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:210)</strong></h2><h2><strong>at org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:222)</strong></h2><h2><strong>at org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:123)</strong></h2><h2><strong>at org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:502)</strong></h2><h2><strong>at org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:171)</strong></h2><h2><strong>at org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:100)</strong></h2><h2><strong>at org.apache.catalina.valves.AccessLogValve.invoke(AccessLogValve.java:953)</strong></h2><h2><strong>at org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:118)</strong></h2><h2><strong>at org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:409)</strong></h2><h2><strong>at org.apache.coyote.http11.AbstractHttp11Processor.process(AbstractHttp11Processor.java:1044)</strong></h2><h2><strong>at org.apache.coyote.AbstractProtocol$AbstractConnectionHandler.process(AbstractProtocol.java:607)</strong></h2><h2><strong>at org.apache.tomcat.util.net.JIoEndpoint$SocketProcessor.run(JIoEndpoint.java:313)</strong></h2><h2><strong>at java.util.concurrent.ThreadPoolExecutor.runWorker(Unknown Source)</strong></h2><h2><strong>at java.util.concurrent.ThreadPoolExecutor$Worker.run(Unknown Source)</strong></h2><h2><strong>at java.lang.Thread.run(Unknown Source)</strong></h2><h2><strong>Caused by: com.sun.mail.smtp.SMTPAddressFailedException: 550 Invalid User: \"xxxxx@163.com\"</strong></h2><h2><br></h2><h2><br></h2><h2><strong>at com.sun.mail.smtp.SMTPTransport.rcptTo(SMTPTransport.java:1686)</strong></h2><h2><strong>... 50 more</strong></h2><h2><br></h2><h2><br></h2><h2><strong>报错提示为邮箱地址有错，看了邮箱地址，完全正确啊，然后看了一下代码，也没错啊，后面上百度搜了一下，还是没发现问题出在哪里，就这样折腾了将近3个小时，终于发现，我发请求的时候多加了双引号，然后报错提示中没看来那个非法地址中的双引号是非法的，以为只是对地址说明。。。、</strong></h2><h2><br></h2><h2><strong>去掉双引号发请求后发邮件成功。</strong></h2><p><br></p>', '今天的心情', '今天做邮箱验证的时候，遇到一个比较奇怪的问题： javax.mail.SendFailedException: Invalid Addresses;   nested exception is: com.sun.mail.smtp.SMTPAddressFailedException: 550 Invalid User: \"13540619068@163.com\" at com.sun.mail.', '0', '0', null, '2018-11-29 20:07:02', '0', '0', '0', null, null, '0', null);
INSERT INTO `s_posts` VALUES ('18', '5', '2018-11-29 20:19:42', '<p>&nbsp;以后呀，我们要是有幸在一起了，我们家务活一定要分工明确，你收拾屋子，我收拾你。</p><p><br></p>', '鑫宇你看到了吗？', ' 以后呀，我们要是有幸在一起了，我们家务活一定要分工明确，你收拾屋子，我收拾你。', '0', '0', null, '2018-11-29 20:19:42', '0', '0', '0', null, null, '0', '/upload/2/f/3/4/b/2/7/0/e15e2b01-1610-4e85-be8c-f04b6cdf8261.jpg');
INSERT INTO `s_posts` VALUES ('19', '5', '2018-11-29 20:25:16', '<p>懂事的人一旦不配合就会被说没良心，任性的人稍微乖巧一回就被夸个不停。</p>', '你怎么还没看见呢？哼╭(╯^╰)╮', '懂事的人一旦不配合就会被说没良心，任性的人稍微乖巧一回就被夸个不停。', '0', '0', null, '2018-11-29 20:25:16', '0', '0', '0', null, null, '0', '/upload/2/d/7/1/c/2/5/0/7dc4af3d-6898-4c7f-8c7f-aebb25e02657.jpg');
INSERT INTO `s_posts` VALUES ('20', '6', '2018-11-29 20:27:38', '今年湖人能夺冠么？\n', null, null, '0', '0', null, '2018-11-29 20:27:38', '0', '0', '0', null, null, '0', null);
INSERT INTO `s_posts` VALUES ('21', '5', '2018-11-29 20:27:39', '<p>&nbsp;遇见的人多了，你才会明白哪些人值得用生命去珍惜，而一些人只适合绕道而行。</p>', '我真的生气了！哄不好的那种╭(╯^╰)╮', ' 遇见的人多了，你才会明白哪些人值得用生命去珍惜，而一些人只适合绕道而行。', '0', '0', null, '2018-11-29 20:27:39', '0', '0', '0', null, null, '0', '/upload/3/2/b/a/9/2/b/0/fc2883b6-3d1b-4e85-bfec-715f6727b561.jpg');
INSERT INTO `s_posts` VALUES ('22', '6', '2018-11-29 20:27:42', '<img class=\"emojione\" alt=\"\" src=\"http://www.share.com/img/emoji/1F600.png?v=1.2.4\"/><img class=\"emojione\" alt=\"\" src=\"http://www.share.com/img/emoji/1F600.png?v=1.2.4\"/><img class=\"emojione\" alt=\"\" src=\"http://www.share.com/img/emoji/1F600.png?v=1.2.4\"/><img class=\"emojione\" alt=\"\" src=\"http://www.share.com/img/emoji/1F600.png?v=1.2.4\"/>', null, null, '0', '0', null, '2018-11-29 20:27:42', '0', '0', '0', null, null, '0', null);
INSERT INTO `s_posts` VALUES ('23', '5', '2018-11-29 20:30:06', '<p>&nbsp;很多时候，碍于面子，你不说，我也不说，很多事情就黄了；很多时候，为了形象，该说的不直说，很多关系就凉了！</p>', '大坏蛋！！！！！！！！！！', ' 很多时候，碍于面子，你不说，我也不说，很多事情就黄了；很多时候，为了形象，该说的不直说，很多关系就凉了！', '0', '0', null, '2018-11-29 20:30:06', '0', '0', '0', null, null, '0', '/upload/2/e/5/3/3/a/6/0/8b395621-8bdf-4458-85f9-3a831e349902.jpg');
INSERT INTO `s_posts` VALUES ('24', '5', '2018-11-29 20:33:41', '<p>傻是我的特长，痴是我的理想，当傻和痴交织在一起的时候，便是我梦境里最美的天堂！别笑我，我就这么痴心，我会傻傻地爱你痴痴地恋你，一直到老。</p>', '你有本事一辈子都不要理我@.@', '傻是我的特长，痴是我的理想，当傻和痴交织在一起的时候，便是我梦境里最美的天堂！别笑我，我就这么痴心，我会傻傻地爱你痴痴地恋你，一直到老。', '0', '0', null, '2018-11-29 20:33:41', '0', '0', '0', null, null, '0', '/upload/3/0/f/7/a/2/9/0/24650727-c2b2-4471-8d51-e20ca435403c.jpg');
INSERT INTO `s_posts` VALUES ('25', '5', '2018-11-29 20:36:10', '<p>不要对一个人太好，因为你终于有一天会发现，对一个人好，时间久了，那个人是会习惯的，然后把这一切看作是理所应当，其实本来是可以蠢到不计代价不顾回报的，但现实总是让人寒了心。其实你明明知道，最卑贱不过感情，最凉不过是人心……</p>', '我好难过~', '不要对一个人太好，因为你终于有一天会发现，对一个人好，时间久了，那个人是会习惯的，然后把这一切看作是理所应当，其实本来是可以蠢到不计代价不顾回报的，但现实总是让人寒了心。其实你明明知道，最卑贱不过感情，最凉不过是人心……', '0', '0', null, '2018-11-29 20:36:10', '0', '0', '0', null, null, '0', '/upload/3/0/1/6/2/a/8/0/9319b393-8b97-441e-b76f-be9a8313d222.jpg');
INSERT INTO `s_posts` VALUES ('26', '5', '2018-11-29 20:39:11', '<p>&nbsp;有些人莫名其妙的闯进你的世界，给了你想要的温暖和陪伴，然而又莫名其妙的消失了。</p>', '人生呐~哎呀。。。哭哭，，', ' 有些人莫名其妙的闯进你的世界，给了你想要的温暖和陪伴，然而又莫名其妙的消失了。', '0', '0', null, '2018-11-29 20:39:11', '0', '0', '0', null, null, '0', '/upload/3/1/d/9/1/a/a/0/688529a2-0948-4de3-bc8c-df4f7e787607.jpg');

-- ----------------------------
-- Table structure for `s_relations`
-- ----------------------------
DROP TABLE IF EXISTS `s_relations`;
CREATE TABLE `s_relations` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `object_type` int(11) NOT NULL,
  `object_id` int(11) NOT NULL,
  `tag_id` int(11) NOT NULL,
  `add_ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_tag_id_idx` (`tag_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of s_relations
-- ----------------------------
INSERT INTO `s_relations` VALUES ('1', '2', '2', '1', '2018-11-29 18:40:22');
INSERT INTO `s_relations` VALUES ('2', '2', '3', '1', '2018-11-29 19:47:46');
INSERT INTO `s_relations` VALUES ('3', '0', '1', '1', '2018-11-29 19:48:35');
INSERT INTO `s_relations` VALUES ('4', '0', '1', '1', '2018-11-29 20:33:41');
INSERT INTO `s_relations` VALUES ('5', '0', '1', '4', '2018-11-29 20:36:10');

-- ----------------------------
-- Table structure for `s_tags`
-- ----------------------------
DROP TABLE IF EXISTS `s_tags`;
CREATE TABLE `s_tags` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tag` varchar(100) NOT NULL,
  `add_ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `cover` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of s_tags
-- ----------------------------
INSERT INTO `s_tags` VALUES ('1', '柯南', '2018-11-29 18:40:21', null);
INSERT INTO `s_tags` VALUES ('2', '还不错', '2018-11-29 19:47:46', null);
INSERT INTO `s_tags` VALUES ('3', '球', '2018-11-29 19:48:35', null);
INSERT INTO `s_tags` VALUES ('4', '❤', '2018-11-29 20:33:41', null);

-- ----------------------------
-- Table structure for `s_users`
-- ----------------------------
DROP TABLE IF EXISTS `s_users`;
CREATE TABLE `s_users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(100) DEFAULT NULL,
  `user_email` varchar(100) NOT NULL,
  `user_pwd` varchar(100) NOT NULL,
  `user_registered_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `user_status` int(11) DEFAULT NULL,
  `user_activationKey` varchar(100) DEFAULT NULL,
  `user_avatar` varchar(100) DEFAULT NULL,
  `user_desc` text,
  `resetpwd_key` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_name` (`user_name`,`user_email`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of s_users
-- ----------------------------
INSERT INTO `s_users` VALUES ('1', 'fxy', '1127762615@qq.com', 'e10adc3949ba59abbe56e057f20f883e', '2018-11-29 17:45:12', '0', '123', '/upload/6/5/c/a/a/b/d/d/26a52c36-b6e5-4f3f-ab2c-70f062e8e172.jpg', null, null);
INSERT INTO `s_users` VALUES ('2', '1111', '1696081318@qq.com', 'e40f01afbb1b9ae3dd6747ced5bca532', '2018-11-29 17:49:15', '0', '123', '/upload/3/6/5/6/e/e/d/4/018acdcb-59b3-46bf-b328-73865f66b010.jpg', '', null);
INSERT INTO `s_users` VALUES ('3', '超', '570312954@qq.com', 'e10adc3949ba59abbe56e057f20f883e', '2018-11-29 18:38:00', '0', '123', '/upload/c/d/e/c/b/4/7/6/7aefb6c9-5556-429b-8062-2fb6e7e10a8b.jpg', '还不错', null);
INSERT INTO `s_users` VALUES ('4', '李飒', '1158590709@qq.com', '80c9ef0fb86369cd25f90af27ef53a9e', '2018-11-29 19:24:46', '0', '123', '/upload/3/0/f/7/a/2/9/0/129336d5-1211-41a0-9c41-63071260f21b.jpg', null, null);
INSERT INTO `s_users` VALUES ('5', '于洁', '1067123641@qq.com', '202cb962ac59075b964b07152d234b70', '2018-11-29 19:30:28', '0', '123', '/upload/5/c/3/4/4/d/6/f/69051e32-913b-4718-8f19-618b93a9f617.jpg', '萌萌哒的小可爱', null);
INSERT INTO `s_users` VALUES ('6', '郭美丽', '1171826369@qq.com', '202cb962ac59075b964b07152d234b70', '2018-11-29 19:55:28', '0', '123', '/upload/7/5/f/7/9/1/7/5/0c7cf93c-3d1f-4954-9147-65bb04238251.jpg', null, null);
INSERT INTO `s_users` VALUES ('7', 'wj', '2847919002@qq.com', '698d51a19d8a121ce581499d7b701668', '2018-11-29 19:57:12', '0', '123', '/defaultimg/0.jpg', null, null);
INSERT INTO `s_users` VALUES ('8', '周猴子', '1596937828@qq.com', '25d55ad283aa400af464c76d713c07ad', '2018-11-29 20:18:45', '0', '123', '/upload/c/b/6/1/0/e/7/b/238afc5f-07ae-42df-91c0-7c6198861e1d.jpg', '我是傻蛋，我怕谁！！！', null);
