package br.com.chat_ai_bff.api;

import br.com.chat_ai_bff.http.RequestIA;
import br.com.chat_ai_bff.persistence.ChatIa;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Path("/search")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class Search {

    @Inject
    Validator validator;

    @GET
    @Path("{identify}")
    public CustomerReturn getAll(
            @PathParam("identify") String identify,
            @QueryParam("sort") @DefaultValue("id") String sortParams,
            @QueryParam("page") @DefaultValue("0") Integer pageIndex,
            @QueryParam("limit") @DefaultValue("10") Integer pageLimit
    ) throws Exception {
        CustomerReturn customer = new CustomerReturn();

        Long CountAll = ChatIa.find("identify", identify).count();
        PanacheQuery<ChatIa> livingPersons = ChatIa.find("identify", Sort.descending(sortParams), identify);

        customer.total_page = CountAll/pageLimit;
        customer.current_page = pageIndex;

        customer.data = livingPersons.page(Page.of(pageIndex, pageLimit)).list();

        return customer;
    }
    @POST
    @Transactional
    @Path("{identify}")
    public Response create(
            @PathParam("identify") String identify,
            ChatIa chatia
    ) throws IOException, InterruptedException {
            ChatIa entity = ChatIa.find("identify", identify).firstResult();
            if (entity == null)
                throw new WebApplicationException("Chat identify " + identify + "does not exist!.", 204);

            Map<String, Object> params = new HashMap<>();
            params.put("search",chatia.search);
            params.put("identify", identify);
            ChatIa searchQuestion = ChatIa.find("search = :search and identify = :identify", params).firstResult();
            if (searchQuestion != null)
                return Response.ok(searchQuestion).status(201).build();


            Set<ConstraintViolation<ChatIa>> violations = validator.validate(chatia);
            if (!violations.isEmpty())
                throw new WebApplicationException("Data invalid", 400);

             RequestIA request = new RequestIA();

             String data = request.requestQuestion(chatia.search);

             chatia.identify = identify;
             chatia.responseText = data.trim();
             chatia.persist();
            return Response.ok(chatia).status(201).build();
    }
}
