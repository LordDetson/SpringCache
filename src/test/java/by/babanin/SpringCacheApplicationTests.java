package by.babanin;

import by.babanin.entity.User;
import by.babanin.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringCacheApplicationTests {
	private static final Logger log = LoggerFactory.getLogger(SpringCacheApplicationTests.class);

	@Autowired
	private UserService userService;

	@Test
	public void get() {
		User lord_detson = userService.create(new User("Lord_Detson", "babanin.dima@gmail.com"));
		User distiks = userService.create(new User("Distiks", "distiks@maul.ru"));

		getAndPrint(lord_detson);
		getAndPrint(distiks);
		getAndPrint(lord_detson);
		getAndPrint(distiks);
	}

	private void getAndPrint(User user) {
		log.info("user found {}", userService.get(user.getId()));
	}

	@Test
	public void create() {
		createAndPrint("Ivan", "ivan@mail.ru");
		createAndPrint("Ivan", "ivan1122@mail.ru");
		createAndPrint("Sergey", "ivan@mail.ru");

		log.info("all entries are below:");
		userService.getAll().forEach(u -> log.info("{}", u.toString()));
	}

	private void createAndPrint(String name, String email) {
		log.info("created user: {}", userService.create(name, email));
	}

	@Test
	public void createAndRefresh() {
		User user1 = userService.createOrReturnCached(new User("Vasya", "vasya@mail.ru"));
		log.info("created user1: {}", user1);

		User user2 = userService.createOrReturnCached(new User("Vasya", "misha@mail.ru"));
		log.info("created user2: {}", user2);

		User user3 = userService.createAndRefreshCache(new User("Vasya", "kolya@mail.ru"));
		log.info("created user3: {}", user3);

		User user4 = userService.createOrReturnCached(new User("Vasya", "petya@mail.ru"));
		log.info("created user4: {}", user4);
	}

	@Test
	public void delete() {
		User user1 = userService.create(new User("Vasya", "vasya@mail.ru"));
		log.info("{}", userService.get(user1.getId()));

		User user2 = userService.create(new User("Vasya", "vasya@mail.ru"));
		log.info("{}", userService.get(user2.getId()));

		userService.delete(user1.getId());
		userService.deleteAndEvict(user2.getId());

		log.info("{}", userService.get(user1.getId()));
		log.info("{}", userService.get(user2.getId()));
	}

	@Test
	public void checkSettings() throws InterruptedException {
		User user1 = userService.createOrReturnCached(new User("Vasya", "vasya@mail.ru"));
		log.info("{}", userService.get(user1.getId()));

		User user2 = userService.createOrReturnCached(new User("Vasya", "vasya@mail.ru"));
		log.info("{}", userService.get(user2.getId()));

		Thread.sleep(1000L);
		User user3 = userService.createOrReturnCached(new User("Vasya", "vasya@mail.ru"));
		log.info("{}", userService.get(user3.getId()));
	}
}
