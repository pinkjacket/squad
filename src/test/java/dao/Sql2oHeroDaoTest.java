package dao;

import models.Hero;
import models.Squad;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.*;

public class Sql2oHeroDaoTest {
    private Sql2oHeroDao heroDao;
    private Connection conn;

    @Before
    public void setUp() throws Exception {
        String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/create.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        heroDao = new Sql2oHeroDao(sql2o);

        conn = sql2o.open();
    }
    @After
    public void tearDown() throws Exception {
        conn.close();
    }

    @Test
    public void addHero() throws Exception {
        Hero testHero = new Hero("Executive Fist");
        int originalHeroId = testHero.getId();
        heroDao.add(testHero);
        assertNotEquals(originalHeroId, testHero.getId());
    }

    @Test
    public void findHeroById() throws Exception {
        Hero testHero = new Hero("Executive Fist");
        heroDao.add(testHero);
        Hero foundHero = heroDao.findById(testHero.getId());
        assertEquals(testHero, foundHero);
    }

    @Test
    public void heroesReturnedFromGetAllHeroes() throws Exception {
        Hero heroOne = new Hero("Executive Fist");
        Hero heroTwo = new Hero("Face Kicker Prime");
        heroDao.add(heroOne);
        heroDao.add(heroTwo);

        assertEquals(2, heroDao.getAll().size());
    }

    @Test
    public void noHerosReturnsEmptyList() throws Exception {
        assertEquals(0, heroDao.getAll().size());
    }

    @Test
    public void updateChangesHeroName() throws Exception {
        String initialName = "Executive Fist";
        Hero hero = new Hero (initialName);
        heroDao.add(hero);

        heroDao.update(hero.getId(), "Godhand", 1);
        Hero updatedHero = heroDao.findById(hero.getId());
        assertNotEquals(initialName, updatedHero.getName());
    }

    @Test
    public void deleteByIdDeletesCorrectTask() throws Exception {
        String initialName = "Executive Fist";
        Hero hero = new Hero (initialName);
        heroDao.add(hero);
        heroDao.deleteById(hero.getId());
        assertEquals(0, heroDao.getAll().size());
    }

    @Test
    public void clearAllClearsAll() throws Exception {
        Hero heroOne = new Hero("Executive Fist");
        Hero heroTwo = new Hero("Face Kicker Prime");
        heroDao.add(heroOne);
        heroDao.add(heroTwo);
        int daoSize = heroDao.getAll().size();
        heroDao.clearAllHeroes();
        assertTrue(daoSize > 0 && daoSize > heroDao.getAll().size()); //this is a little overcomplicated, but illustrates well how we might use `assertTrue` in a different way.
    }
}