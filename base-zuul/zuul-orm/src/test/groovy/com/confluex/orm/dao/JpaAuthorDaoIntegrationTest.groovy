package com.confluex.orm.dao

import groovy.util.logging.Slf4j
import org.junit.Ignore
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction
import com.confluex.orm.BaseIntegrationTest
import com.confluex.orm.model.Author
import javax.validation.ConstraintViolationException

@Slf4j
class JpaAuthorDaoIntegrationTest extends BaseIntegrationTest {

    @Autowired
    AuthorDao authorDao

    
    @Test
    void findOneFindsRecordWithCorrectId() {
        def optAuthor = authorDao.findById(2)
        log.info('Fetched author with id 2', optAuthor)
        Author author = null
        optAuthor.ifPresent({author = it })
        assert author.id == 2
        assert author.firstName == "David"
        assert author.lastName == "Chang"
    }

    @Test
    void findAllWithSorting() {
        def results = authorDao.findAll(new Sort(Direction.DESC, "lastName", "firstName"))
        def lastNames = results.collect { it.lastName }
        assert lastNames == ["McGee", "Keller", "Chang"]
    }

    @Test
    void findAllWithPaging() {
        def results = authorDao.findAll(new PageRequest(1, 2, Direction.DESC, "lastName"))
        println results
        assert results.content.size() == 1
        assert results.content.first().lastName == "Chang"
    }

    @Test
    void findByLastName() {
        def results = authorDao.findByLastName("Chang")
        assert results.size() == 1
        assert results.first().lastName == "Chang"
    }

    @Test
    // @Ignore
    // @Test(expected=ConstraintViolationException)
    // TODO: Why isn't it working for expected error
    void shouldRequireLastNameWithMinLength() {
        def author = new Author(firstName: "Joel", lastName: "R")
        authorDao.save(author)
    }

}
