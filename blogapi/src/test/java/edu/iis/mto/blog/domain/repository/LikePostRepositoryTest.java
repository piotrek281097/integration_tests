package edu.iis.mto.blog.domain.repository;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.model.User;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.matches;

@RunWith(SpringRunner.class)
@DataJpaTest
public class LikePostRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BlogPostRepository blogPostRepository;

    @Autowired
    private LikePostRepository likePostRepository;

    private User user;
    private BlogPost blogPost;
    private LikePost likePost;

    @Before
    public void setUp() {
        user = new User();
        user.setFirstName("Jan");
        user.setLastName("Klink");
        user.setEmail("john@domain.com");
        user.setAccountStatus(AccountStatus.NEW);

        List<LikePost> likePosts = new ArrayList<>();

        blogPost = new BlogPost();
        blogPost.setUser(user);
        blogPost.setEntry("blogPostEntry");
        blogPost.setLikes(likePosts);

        likePost = new LikePost();
        likePost.setPost(blogPost);
        likePost.setUser(user);

        likePosts.add(likePost);
    }



    @Test
    public void shouldFindNoLikePostsIfRepositoryIsEmpty() {

        List<LikePost> likePosts = likePostRepository.findAll();

        Assert.assertThat(likePosts, Matchers.hasSize(0));
    }

    @Test
    public void shouldFindOneLikePostIfRepositoryContainsOneLikePostEntity() {

        userRepository.save(user);
        blogPostRepository.save(blogPost);
        entityManager.persist(likePost);

        List<LikePost> likePosts = likePostRepository.findAll();

        Assert.assertThat(likePosts, Matchers.hasSize(1));
        Assert.assertThat(likePosts.get(0).getUser(), Matchers.sameInstance(user));
    }

    @Test
    public void shouldStoreANewLikePost() {

        entityManager.persist(user);
        entityManager.persist(blogPost);
        entityManager.persist(likePost);

        LikePost persistedLikePost = likePostRepository.save(likePost);

        Assert.assertThat(persistedLikePost.getId(), Matchers.notNullValue());
    }

    @Test
    public void shouldModifyLikePostByChangingUser() {

        entityManager.persist(user);
        entityManager.persist(blogPost);

        User user2 = new User();
        user2.setFirstName("Janusz");
        user2.setLastName("Januszowy");
        user2.setEmail("janusz@wp.pl");
        user2.setAccountStatus(AccountStatus.NEW);
        entityManager.persist(user2);

        likePost.setUser(user2);
        entityManager.persist(likePost);

        LikePost persistedLikePost = likePostRepository.save(likePost);

        Assert.assertThat(persistedLikePost.getUser(), Matchers.sameInstance(user2));
    }

    @Test
    public void shouldModifyLikePostByChangingBlogPost() {

        entityManager.persist(user);
        entityManager.persist(blogPost);

        BlogPost blogPost2 = new BlogPost();
        blogPost2.setUser(user);
        blogPost2.setEntry("newEntry");
        entityManager.persist(blogPost2);

        likePost.setPost(blogPost2);
        entityManager.persist(likePost);

        LikePost persistedLikePost = likePostRepository.save(likePost);

        Assert.assertThat(persistedLikePost.getPost(), Matchers.sameInstance(blogPost2));
    }

    @Test
    public void shouldFindLikePostByUserAndBlogPost() {

        userRepository.save(user);
        blogPostRepository.save(blogPost);
        likePostRepository.save(likePost);

        Optional<LikePost> likePosts = likePostRepository.findByUserAndPost(user, blogPost);

        Assert.assertThat(likePosts.isPresent(), is(true));
    }

    @Test
    public void shouldNotFindLikePostIfBlogPostIsWrong() {

        userRepository.save(user);

        BlogPost blogPost2 = new BlogPost();
        blogPost2.setUser(user);
        blogPost2.setEntry("newEntry");
        blogPostRepository.save(blogPost2);
        entityManager.persist(blogPost2);

        Optional<LikePost> likePosts = likePostRepository.findByUserAndPost(user, blogPost2);

        Assert.assertThat(likePosts.isPresent(), is(false));
    }

    @Test
    public void shouldNotFindLikePostIfUserIsWrong() {

        userRepository.save(user);
        blogPostRepository.save(blogPost);
        entityManager.persist(likePost);

        User user2 = new User();
        user2.setEmail("janusz@wp.pl");
        user2.setAccountStatus(AccountStatus.NEW);

        entityManager.persist(user2);

        Optional<LikePost> likePosts = likePostRepository.findByUserAndPost(user2, blogPost);

        Assert.assertThat(likePosts.isPresent(), is(false));
    }


}
