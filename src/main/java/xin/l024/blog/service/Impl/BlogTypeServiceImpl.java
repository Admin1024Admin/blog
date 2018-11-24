package xin.l024.blog.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xin.l024.blog.entity.BlogType;
import xin.l024.blog.entity.User;
import xin.l024.blog.repository.BlogTypeRepository;
import xin.l024.blog.service.BlogTypeService;

import java.util.List;

@Service
public class BlogTypeServiceImpl implements BlogTypeService {
    @Autowired
    private BlogTypeRepository blogTypeRepository;

    @Override
    public List<BlogType> fingByUserId(Long uid) {
        return blogTypeRepository.findByUserId(uid);
    }

    @Override
    public BlogType savaType(User user, String typeName) {
        BlogType blogType = new BlogType();
        blogType.setTypeName(typeName);
        blogType.setUser(user);
        return blogTypeRepository.save(blogType);
    }


}
