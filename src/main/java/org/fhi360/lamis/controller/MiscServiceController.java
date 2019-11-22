/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.controller;

import io.swagger.annotations.Api;
import org.fhi360.lamis.utility.MiscService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author user10
 */
@RestController
@RequestMapping(value = "/messageService")
@Api(tags = "Message Service" , description = " ")
public class MiscServiceController {

    @RequestMapping(value = "/services", method = RequestMethod.GET)
    public String miscService() {
        new MiscService().task();
        return "SUCCESS";
    }

}
