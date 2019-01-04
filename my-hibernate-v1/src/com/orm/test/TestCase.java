package com.orm.test;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.orm.core.HibernateUtil;
import com.orm.core.Query;
import com.orm.core.Session;
import com.orm.core.Transaction;
import com.orm.model.Answer;
import com.orm.model.User;

public class TestCase {
	public static void main(String[] args) {
		testAnswer();
	}

	private static void testAnswer() {
		Session session = HibernateUtil.getAnnoSessionFactory().getCurrentSession();// 通过Session工厂获取Session对象
		session.beginTransaction(); // 开始事务
		Answer record = new Answer();
		//record.setId(1);
		record.setChoiceId(112266);
		record.setEnabled(true);
		record.setQuestionnaireId(2233);
		record.setUserId(11);
		record.setVersion(0);
		record.setCreateTime(new Date());
		record.setUpdateTime(new Date());
		session.save(record);
		// session.update(record);
		// session.delete(Answer.class, 1);
		/*
		 * String hql = "from User where phone=:phone and userName=:name"; Query<User>
		 * query = session.createQuery(hql,User.class); query.setParameter("phone",
		 * "1807657569"); query.setParameter("name", "张三"); List<User> list =
		 * query.list(); System.out.println(JSONObject.toJSONString(list));
		 */

		String hql = "from Answer where enabled=:cEnabled and choiceId=:cId";
		Query<Answer> query = session.createQuery(hql, Answer.class);
		query.setParameter("cEnabled", true);
		query.setParameter("cId", 112266);
		List<Answer> list = query.list();
		System.out.println(JSONObject.toJSONString(list));

		// String hql = "from Answer where choiceId=:cId";
		// Query<Answer> query = session.createQuery(hql, Answer.class);
		// query.setParameter("cId", 1122);
		// List<Answer> list = query.list();
		// System.out.println(JSONObject.toJSONString(list));
		// Query query1 = session.createQuery();
		// System.out.println(JSONObject.toJSONString(query1.get(Answer.class, 98724)));
		session.getTransaction().commit(); // 提交事务
		HibernateUtil.getAnnoSessionFactory().close();
	}

	private static void testUser() {
		Session session = HibernateUtil.getXMLSessionFactory().getCurrentSession();// 通过Session工厂获取Session对象
		Transaction transaction=session.beginTransaction(); // 开始事务
		User user = new User();
		user.setUserName("张三");
		user.setPassword("1234562");
		user.setPhone("1807657569");
		user.setUserId(2);
		session.save(user);
		// session.update(user);
		// session.delete(User.class, 2);

		String hql = "from User where phone=:phone and userName=:name";
		Query<User> query = session.createQuery(hql, User.class);
		query.setParameter("phone", "1807657569");
		query.setParameter("name", "张三");
		List<User> list = query.list();
		System.out.println(JSONObject.toJSONString(list));

		/*
		 * String hql = "from User where userId=:idx"; Query<User> query =
		 * session.createQuery(hql, User.class); query.setParameter("idx", 12);
		 * List<User> list = query.list();
		 * System.out.println(JSONObject.toJSONString(list));
		 */

		/*
		 * String hql = "from User where userName=:userName"; Query<User> query =
		 * session.createQuery(hql, User.class); query.setParameter("userName", "张三");
		 * List<User> list = query.list();
		 * System.out.println(JSONObject.toJSONString(list));
		 */
		// Query query1 = session.createQuery();
		// System.out.println(query1.get(User.class, 13));
		transaction.commit(); // 提交事务
		HibernateUtil.getXMLSessionFactory().close();
	}
}
