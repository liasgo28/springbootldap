package br.com.diego.springbootldap.service;
import static org.springframework.ldap.query.LdapQueryBuilder.query;

import java.util.List;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.query.SearchScope;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.stereotype.Service;

import br.com.diego.springbootldap.model.Pessoa;

@Service
public class PessoaService {
	private static final Integer THREE_SECONDS = 3000;

    @Autowired
    private LdapTemplate ldapTemplate;

    public List<Pessoa> getPessoaNamesByLastName(String lastName) {

        LdapQuery query = query()
                .searchScope(SearchScope.SUBTREE)
                .timeLimit(THREE_SECONDS)
                .countLimit(10)
                .attributes("cn")
                .base(LdapUtils.emptyLdapName())
                .where("objectclass").is("Pessoa")
                .and("sn").not().is(lastName)
                .and("sn").like("j*hn")
                .and("uid").isPresent();

        return ldapTemplate.search(query, new PessoaAttributesMapper());
    }

    public List<Pessoa> getPessoaNamesByLastName2(String lastName) {

        SearchControls sc = new SearchControls();
        sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
        sc.setTimeLimit(THREE_SECONDS);
        sc.setCountLimit(10);
        sc.setReturningAttributes(new String[]{"cn"});

        String filter = "(&(objectclass=Pessoa)(sn=" + lastName + "))";
        return ldapTemplate.search(LdapUtils.emptyLdapName(), filter, sc, new PessoaAttributesMapper());
    }

    public List<Pessoa> getPessoaNamesByLastName3(String lastName) {

        SearchControls sc = new SearchControls();
        sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
        sc.setTimeLimit(THREE_SECONDS);
        sc.setCountLimit(10);
        sc.setReturningAttributes(new String[]{"cn"});

        AndFilter filter = new AndFilter();
        filter.and(new EqualsFilter("objectclass", "Pessoa"));
        filter.and(new EqualsFilter("sn", lastName));

        return ldapTemplate.search(LdapUtils.emptyLdapName(), filter.encode(), sc, new PessoaAttributesMapper());
    }

    /**
     * Custom Pessoa attributes mapper, maps the attributes to the Pessoa POJO
     */
    private class PessoaAttributesMapper implements AttributesMapper<Pessoa> {
        public Pessoa mapFromAttributes(Attributes attrs) throws NamingException {
            Pessoa Pessoa = new Pessoa();
            Pessoa.setFullName((String)attrs.get("cn").get());

            Attribute sn = attrs.get("sn");
            if (sn != null){
                Pessoa.setLastName((String)sn.get());
            }
            return Pessoa;
        }
    }
}
