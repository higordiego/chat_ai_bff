package br.com.chat_ai_bff.api;


import br.com.chat_ai_bff.persistence.ChatIa;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class CustomerReturn {
    public List<ChatIa> data;
    public Long total_page;
    public Integer current_page;
}