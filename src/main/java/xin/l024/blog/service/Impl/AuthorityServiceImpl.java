package xin.l024.blog.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xin.l024.blog.entity.Authority;
import xin.l024.blog.repository.AuthorityRepository;
import xin.l024.blog.service.AuthorityService;

@Service
public class AuthorityServiceImpl implements AuthorityService {
    @Autowired
    private AuthorityRepository authorityRepository;
    @Override
    public Authority getAuthorityById(Long id) {
        return authorityRepository.findById(id).get();
    }
}
