package com.orm.test;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.orm.core.HibernateUtil;
import com.orm.core.Query;
import com.orm.core.Session;
import com.orm.model.Answer;
import com.orm.model.User;

public class TestCase {
	public static void main(String[] args) {
		testAnswer();
	}

	private static void testAnswer() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();// ͨ��Session������ȡSession����
		session.beginTransaction(); // ��ʼ����
		Answer record = new Answer();
		record.setId(1);
		record.setChoiceId(1122);
		record.setEnabled(true);
		record.setQuestionnaireId(2233);
		record.setUserId(11);
		record.setVersion(0);
		record.setCreateTime(new Date());
		record.setUpdateTime(new Date());
		// session.save(record);
		// session.update(record);
		//session.delete(Answer.class, 1);
		/*
		 * String hql = "from User where phone=:phone and userName=:name"; Query<User>
		 * query = session.createQuery(hql,User.class); query.setParameter("phone",
		 * "1807657569"); query.setParameter("name", "����"); List<User> list =
		 * query.list(); System.out.println(JSONObject.toJSONString(list));
		 */

		String hql = "from Answer where enabled=:cEnabled and choiceId=:cId";
		Query<Answer> query = session.createQuery(hql, Answer.class);
		query.setParameter("cEnabled", true);
		query.setParameter("cId", 1122);
		List<Answer> list = query.list();
		System.out.println(JSONObject.toJSONString(list));

		// String hql = "from Answer where choiceId=:cId";
		// Query<Answer> query = session.createQuery(hql, Answer.class);
		// query.setParameter("cId", 1122);
		// List<Answer> list = query.list();
		// System.out.println(JSONObject.toJSONString(list));
		//Query query1 = session.createQuery();
		//System.out.println(JSONObject.toJSONString(query1.get(Answer.class, 98724)));
		session.getTransaction().commit(); // �ύ����
		HibernateUtil.getSessionFactory().close();
	}

	@SuppressWarnings("unused")
	private static void testUser() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();// ͨ��Session������ȡSession����
		session.beginTransaction(); // ��ʼ����
		User user = new User();
		user.setUserName("����");
		user.setPassword("1234562");
		user.setPhone("1807657569");
		session.save(user);
		session.update(user);
		session.delete(User.class, 2);
		/*
		 * String hql = "from User where phone=:phone and userName=:name"; Query<User>
		 * query = session.createQuery(hql,User.class); query.setParameter("phone",
		 * "1807657569"); query.setParameter("name", "����"); List<User> list =
		 * query.list(); System.out.println(JSONObject.toJSONString(list));
		 */
		/*
		 * String hql = "from User where userId=:idx"; Query<User> query =
		 * session.createQuery(hql,User.class); query.setParameter("idx",1); List<User>
		 * list = query.list(); System.out.println(JSONObject.toJSONString(list));
		 */
		// String hql = "from User where userName=:userName";
		// Query<User> query = session.createQuery(hql, User.class);
		// query.setParameter("userName", "����");
		// List<User> list = query.list();
		// System.out.println(JSONObject.toJSONString(list));
		// Query query1 = session.createQuery();
		// System.out.println(query1.get(User.class, 1));
		session.getTransaction().commit(); // �ύ����
		HibernateUtil.getSessionFactory().close();
	}
}
