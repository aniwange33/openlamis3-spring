/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author User10
 */
@Entity
@Table(name = "Hts")
@Data
public class Hts extends TransactionEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "HTS_ID")
    private Long htsId;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "CLIENT_CODE")
    private String clientCode;

    @Size(max = 25)
    @Column(name = "HOSPITAL_NUM")
    private String hospitalNum;

    @Column(name = "DATE_VISIT")
    private LocalDate dateVisit;

    @Size(max = 100)
    @Column(name = "REFERRED_FROM")
    private String referredFrom;

    @Size(max = 100)
    @Column(name = "TESTING_SETTING")
    private String testingSetting;

    @Size(max = 100)
    @Column(name = "SURNAME")
    private String surname;

    @Size(max = 100)
    @Column(name = "OTHER_NAMES")
    private String otherNames;

    @Column(name = "DATE_BIRTH")
    private LocalDate dateBirth;

    @Column(name = "AGE")
    private Integer age;

    @Size(max = 15)
    @Column(name = "AGE_UNIT")
    private String ageUnit;

    @Size(max = 50)
    @Column(name = "PHONE")
    private String phone;

    @Size(max = 200)
    @Column(name = "ADDRESS")
    private String address;

    @Size(max = 20)
    @Column(name = "GENDER")
    private String gender;

    @Size(max = 10)
    @Column(name = "FIRST_TIME_VISIT")
    private String firstTimeVisit;

    @Size(max = 20)
    @Column(name = "MARITAL_STATUS")
    private String maritalStatus;

    @Column(name = "NUM_CHILDREN")
    private Integer numChildren;

    @Column(name = "NUM_WIVES")
    private Integer numWives;

    @Size(max = 100)
    @Column(name = "TYPE_COUNSELING")
    private String typeCounseling;

    @Size(max = 50)
    @Column(name = "INDEX_CLIENT")
    private String indexClient;

    @Size(max = 50)
    @Column(name = "TYPE_INDEX")
    private String typeIndex;

    @Size(max = 100)
    @Column(name = "INDEX_CLIENT_CODE")
    private String indexClientCode;

    @Column(name = "KNOWLEDGE_ASSESSMENT1")
    private Integer knowledgeAssessment1;

    @Column(name = "KNOWLEDGE_ASSESSMENT2")
    private Integer knowledgeAssessment2;

    @Column(name = "KNOWLEDGE_ASSESSMENT3")
    private Integer knowledgeAssessment3;

    @Column(name = "KNOWLEDGE_ASSESSMENT4")
    private Integer knowledgeAssessment4;

    @Column(name = "KNOWLEDGE_ASSESSMENT5")
    private Integer knowledgeAssessment5;

    @Column(name = "KNOWLEDGE_ASSESSMENT6")
    private Integer knowledgeAssessment6;

    @Column(name = "KNOWLEDGE_ASSESSMENT7")
    private Integer knowledgeAssessment7;

    @Column(name = "RISK_ASSESSMENT1")
    private Integer riskAssessment1;

    @Column(name = "RISK_ASSESSMENT2")
    private Integer riskAssessment2;

    @Column(name = "RISK_ASSESSMENT3")
    private Integer riskAssessment3;

    @Column(name = "RISK_ASSESSMENT4")
    private Integer riskAssessment4;

    @Column(name = "RISK_ASSESSMENT5")
    private Integer riskAssessment5;

    @Column(name = "RISK_ASSESSMENT6")
    private Integer riskAssessment6;

    @Column(name = "TB_SCREENING1")
    private Integer tbScreening1;

    @Column(name = "TB_SCREENING2")
    private Integer tbScreening2;

    @Column(name = "TB_SCREENING3")
    private Integer tbScreening3;

    @Column(name = "TB_SCREENING4")
    private Integer tbScreening4;

    @Column(name = "STI_SCREENING1")
    private Integer stiScreening1;

    @Column(name = "STI_SCREENING2")
    private Integer stiScreening2;

    @Column(name = "STI_SCREENING3")
    private Integer stiScreening3;

    @Column(name = "STI_SCREENING4")
    private Integer stiScreening4;

    @Column(name = "STI_SCREENING5")
    private Integer stiScreening5;

    @Size(max = 100)
    @Column(name = "HIV_TEST_RESULT")
    private String hivTestResult;

    @Size(max = 100)
    @Column(name = "TESTED_HIV")
    private String testedHiv;

    @Column(name = "POST_TEST1")
    private Integer postTest1;

    @Column(name = "POST_TEST2")
    private Integer postTest2;

    @Column(name = "POST_TEST3")
    private Integer postTest3;

    @Column(name = "POST_TEST4")
    private Integer postTest4;

    @Column(name = "POST_TEST5")
    private Integer postTest5;

    @Column(name = "POST_TEST6")
    private Integer postTest6;

    @Column(name = "POST_TEST7")
    private Integer postTest7;

    @Column(name = "POST_TEST8")
    private Integer postTest8;

    @Column(name = "POST_TEST9")
    private Integer postTest9;

    @Column(name = "POST_TEST10")
    private Integer postTest10;

    @Column(name = "POST_TEST11")
    private Integer postTest11;

    @Column(name = "POST_TEST12")
    private Integer postTest12;

    @Column(name = "POST_TEST13")
    private Integer postTest13;

    @Column(name = "POST_TEST14")
    private Integer postTest14;

    @Size(max = 100)
    @Column(name = "SYPHILIS_TEST_RESULT")
    private String syphilisTestResult;

    @Size(max = 100)
    @Column(name = "HEPATITISB_TEST_RESULT")
    private String hepatitisbTestResult;

    @Size(max = 100)
    @Column(name = "HEPATITISC_TEST_RESULT")
    private String hepatitiscTestResult;

    @Size(max = 300)
    @Column(name = "NOTE")
    private String note;

    @Size(max = 100)
    @Column(name = "STI_REFERRED")
    private String stiReferred;

    @Size(max = 100)
    @Column(name = "TB_REFERRED")
    private String tbReferred;

    @Size(max = 100)
    @Column(name = "ART_REFERRED")
    private String artReferred;

    @Column(name = "LONGITUDE")
    private Double longitude;

    @Column(name = "LATITUDE")
    private Double latitude;

    @Column(name = "DATE_REGISTRATION")
    private LocalDate dateRegistration;

    @Column(name = "DATE_STARTED")
    private LocalDate dateStarted;

    @OneToOne(cascade = CascadeType.ALL)
    private Assessment assessment;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "hts")
    private List<IndexContact> indexContacts;
}
