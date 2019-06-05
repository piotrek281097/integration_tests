package edu.iis.mto.blog.domain;

import edu.iis.mto.blog.domain.errors.DomainError;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.repository.BlogPostRepository;
import edu.iis.mto.blog.domain.repository.LikePostRepository;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import edu.iis.mto.blog.api.request.UserRequest;
import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.User;
import edu.iis.mto.blog.domain.repository.UserRepository;
import edu.iis.mto.blog.mapper.BlogDataMapper;
import edu.iis.mto.blog.services.BlogService;

import javax.jws.soap.SOAPBinding;

import java.util.Optional;

import static com.sun.javaws.JnlpxArgs.verify;
import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogManagerTest {

    @MockBean
    UserRepository userRepository;

    @MockBean
    LikePostRepository likePostRepository;

    @MockBean
    BlogPostRepository blogPostRepository;

    @Autowired
    BlogDataMapper dataMapper;

    @Autowired
    BlogService blogService;

    private User userConfirmed, userNotConfirmed;
    private BlogPost blogPost;

    @Before
    public void setup() {
        userConfirmed = new User();
        userConfirmed.setFirstName("Jan");
        userConfirmed.setLastName("Klink");
        userConfirmed.setEmail("john@domain.com");
        userConfirmed.setAccountStatus(AccountStatus.CONFIRMED);
        userConfirmed.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(userConfirmed));

        userNotConfirmed = new User();
        userNotConfirmed.setFirstName("Janek");
        userNotConfirmed.setLastName("Klinkek");
        userNotConfirmed.setEmail("johnek@domain.com");
        userNotConfirmed.setAccountStatus(AccountStatus.NEW);
        userNotConfirmed.setId(2L);
        when(userRepository.findById(2L)).thenReturn(Optional.ofNullable(userNotConfirmed));

        blogPost = new BlogPost();
        blogPost.setUser(userNotConfirmed);
        blogPost.setEntry("blogPostEntry");
        blogPost.setId(3L);
        when(blogPostRepository.findById(3L)).thenReturn(Optional.ofNullable(blogPost));
    }

    @Test
    public void creatingNewUserShouldSetAccountStatusToNEW() {
        blogService.createUser(new UserRequest("John", "Steward", "john@domain.com"));
        ArgumentCaptor<User> userParam = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userRepository).save(userParam.capture());
        User user = userParam.getValue();
        Assert.assertThat(user.getAccountStatus(), Matchers.equalTo(AccountStatus.NEW));
    }

    @Test
    public void addingLikePostShouldBePossibleOnlyByUserWithStatusConfirmed() {
        blogService.addLikeToPost(userConfirmed.getId(), blogPost.getId());
        ArgumentCaptor<LikePost> likePostParam = ArgumentCaptor.forClass(LikePost.class);
        Mockito.verify(likePostRepository).save(likePostParam.capture());
        LikePost likePost = likePostParam.getValue();

        Assert.assertThat(likePost.getUser(), Matchers.sameInstance(userConfirmed));
        Assert.assertThat(likePost.getPost(), Matchers.sameInstance(blogPost));

    }

    @Test(expected = DomainError.class)
    public void addingLikeByUserWithStatusOtherThanConfirmedShouldThrowDomainError() {
        blogService.addLikeToPost(userNotConfirmed.getId(), blogPost.getId());
    }

    @Test(expected = DomainError.class)
    public void addingLikeByUserWithStatusOtherThanConfirmedShouldThrowDomainErrorSecondOption() {
        userNotConfirmed.setAccountStatus(AccountStatus.REMOVED);
        blogService.addLikeToPost(userNotConfirmed.getId(), blogPost.getId());
    }

}
