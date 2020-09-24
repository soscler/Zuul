package com.confluex.orm.dao

import com.confluex.orm.BaseIntegrationTest
import com.confluex.orm.model.Book
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class JpaBookDaoIntegrationTest extends BaseIntegrationTest {

    @Autowired
    BookDao dao

    @Test
    void findOneShouldReturnCorrectRecord() {
        def optBook = dao.findById(4)
        Book book = null
        optBook.ifPresent({book = it})
        assert book.id == 4
        assert book.title == "The French Laundry Cookbook"
        assert book.author.lastName == "Keller"
    }


}
