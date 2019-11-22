package org.fhi360.lamis.controller.mapstruct;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.fhi360.lamis.model.Assessment;
import org.fhi360.lamis.model.Facility;
import org.fhi360.lamis.model.Hts;
import org.fhi360.lamis.model.dto.HtsDto;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-11-21T15:49:45+0100",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 1.8.0_231 (Oracle Corporation)"
)
@Component
public class HtsMapperImpl implements HtsMapper {

    @Override
    public HtsDto htsToDto(Hts hts) {
        if ( hts == null ) {
            return null;
        }

        HtsDto htsDto = new HtsDto();

        htsDto.setFacilityId( htsFacilityId( hts ) );
        if ( hts.getDateStarted() != null ) {
            htsDto.setDateStarted( DateTimeFormatter.ofPattern( "MM/dd/yyyy" ).format( hts.getDateStarted() ) );
        }
        if ( hts.getDateRegistration() != null ) {
            htsDto.setDateRegistration( DateTimeFormatter.ofPattern( "MM/dd/yyyy" ).format( hts.getDateRegistration() ) );
        }
        if ( hts.getDateVisit() != null ) {
            htsDto.setDateVisit( DateTimeFormatter.ofPattern( "MM/dd/yyyy" ).format( hts.getDateVisit() ) );
        }
        if ( hts.getDateBirth() != null ) {
            htsDto.setDateBirth( DateTimeFormatter.ofPattern( "MM/dd/yyyy" ).format( hts.getDateBirth() ) );
        }
        htsDto.setAssessmentId( htsAssessmentAssessmentId( hts ) );
        htsDto.setHtsId( hts.getHtsId() );
        htsDto.setClientCode( hts.getClientCode() );
        htsDto.setHospitalNum( hts.getHospitalNum() );
        htsDto.setReferredFrom( hts.getReferredFrom() );
        htsDto.setTestingSetting( hts.getTestingSetting() );
        htsDto.setSurname( hts.getSurname() );
        htsDto.setOtherNames( hts.getOtherNames() );
        htsDto.setAge( hts.getAge() );
        htsDto.setAgeUnit( hts.getAgeUnit() );
        htsDto.setPhone( hts.getPhone() );
        htsDto.setAddress( hts.getAddress() );
        htsDto.setGender( hts.getGender() );
        htsDto.setFirstTimeVisit( hts.getFirstTimeVisit() );
        htsDto.setMaritalStatus( hts.getMaritalStatus() );
        htsDto.setNumChildren( hts.getNumChildren() );
        htsDto.setNumWives( hts.getNumWives() );
        htsDto.setTypeCounseling( hts.getTypeCounseling() );
        htsDto.setIndexClient( hts.getIndexClient() );
        htsDto.setTypeIndex( hts.getTypeIndex() );
        htsDto.setIndexClientCode( hts.getIndexClientCode() );
        htsDto.setKnowledgeAssessment1( hts.getKnowledgeAssessment1() );
        htsDto.setKnowledgeAssessment2( hts.getKnowledgeAssessment2() );
        htsDto.setKnowledgeAssessment3( hts.getKnowledgeAssessment3() );
        htsDto.setKnowledgeAssessment4( hts.getKnowledgeAssessment4() );
        htsDto.setKnowledgeAssessment5( hts.getKnowledgeAssessment5() );
        htsDto.setKnowledgeAssessment6( hts.getKnowledgeAssessment6() );
        htsDto.setKnowledgeAssessment7( hts.getKnowledgeAssessment7() );
        htsDto.setRiskAssessment1( hts.getRiskAssessment1() );
        htsDto.setRiskAssessment2( hts.getRiskAssessment2() );
        htsDto.setRiskAssessment3( hts.getRiskAssessment3() );
        htsDto.setRiskAssessment4( hts.getRiskAssessment4() );
        htsDto.setRiskAssessment5( hts.getRiskAssessment5() );
        htsDto.setRiskAssessment6( hts.getRiskAssessment6() );
        htsDto.setTbScreening1( hts.getTbScreening1() );
        htsDto.setTbScreening2( hts.getTbScreening2() );
        htsDto.setTbScreening3( hts.getTbScreening3() );
        htsDto.setTbScreening4( hts.getTbScreening4() );
        htsDto.setStiScreening1( hts.getStiScreening1() );
        htsDto.setStiScreening2( hts.getStiScreening2() );
        htsDto.setStiScreening3( hts.getStiScreening3() );
        htsDto.setStiScreening4( hts.getStiScreening4() );
        htsDto.setStiScreening5( hts.getStiScreening5() );
        htsDto.setHivTestResult( hts.getHivTestResult() );
        htsDto.setTestedHiv( hts.getTestedHiv() );
        htsDto.setPostTest1( hts.getPostTest1() );
        htsDto.setPostTest2( hts.getPostTest2() );
        htsDto.setPostTest3( hts.getPostTest3() );
        htsDto.setPostTest4( hts.getPostTest4() );
        htsDto.setPostTest5( hts.getPostTest5() );
        htsDto.setPostTest6( hts.getPostTest6() );
        htsDto.setPostTest7( hts.getPostTest7() );
        htsDto.setPostTest8( hts.getPostTest8() );
        htsDto.setPostTest9( hts.getPostTest9() );
        htsDto.setPostTest10( hts.getPostTest10() );
        htsDto.setPostTest11( hts.getPostTest11() );
        htsDto.setPostTest12( hts.getPostTest12() );
        htsDto.setPostTest13( hts.getPostTest13() );
        htsDto.setPostTest14( hts.getPostTest14() );
        htsDto.setSyphilisTestResult( hts.getSyphilisTestResult() );
        htsDto.setHepatitisbTestResult( hts.getHepatitisbTestResult() );
        htsDto.setHepatitiscTestResult( hts.getHepatitiscTestResult() );
        htsDto.setNote( hts.getNote() );
        htsDto.setArtReferred( hts.getArtReferred() );
        htsDto.setTbReferred( hts.getTbReferred() );
        htsDto.setStiReferred( hts.getStiReferred() );
        htsDto.setLatitude( hts.getLatitude() );
        htsDto.setLongitude( hts.getLongitude() );

        return htsDto;
    }

    @Override
    public List<Hts> listDtoToHts(List<HtsDto> dtos) {
        if ( dtos == null ) {
            return null;
        }

        List<Hts> list = new ArrayList<Hts>( dtos.size() );
        for ( HtsDto htsDto : dtos ) {
            list.add( dtoToHts( htsDto ) );
        }

        return list;
    }

    @Override
    public Hts dtoToHts(HtsDto htsDto) {
        if ( htsDto == null ) {
            return null;
        }

        Hts hts = new Hts();

        hts.setAssessment( htsDtoToAssessment( htsDto ) );
        hts.setFacility( htsDtoToFacility( htsDto ) );
        if ( htsDto.getDateStarted() != null ) {
            hts.setDateStarted( LocalDate.parse( htsDto.getDateStarted(), DateTimeFormatter.ofPattern( "MM/dd/yyyy" ) ) );
        }
        if ( htsDto.getDateRegistration() != null ) {
            hts.setDateRegistration( LocalDate.parse( htsDto.getDateRegistration(), DateTimeFormatter.ofPattern( "MM/dd/yyyy" ) ) );
        }
        if ( htsDto.getDateVisit() != null ) {
            hts.setDateVisit( LocalDate.parse( htsDto.getDateVisit(), DateTimeFormatter.ofPattern( "MM/dd/yyyy" ) ) );
        }
        if ( htsDto.getDateBirth() != null ) {
            hts.setDateBirth( LocalDate.parse( htsDto.getDateBirth(), DateTimeFormatter.ofPattern( "MM/dd/yyyy" ) ) );
        }
        hts.setFacilityId( htsDto.getFacilityId() );
        hts.setHtsId( htsDto.getHtsId() );
        hts.setClientCode( htsDto.getClientCode() );
        hts.setHospitalNum( htsDto.getHospitalNum() );
        hts.setReferredFrom( htsDto.getReferredFrom() );
        hts.setTestingSetting( htsDto.getTestingSetting() );
        hts.setSurname( htsDto.getSurname() );
        hts.setOtherNames( htsDto.getOtherNames() );
        hts.setAge( htsDto.getAge() );
        hts.setAgeUnit( htsDto.getAgeUnit() );
        hts.setPhone( htsDto.getPhone() );
        hts.setAddress( htsDto.getAddress() );
        hts.setGender( htsDto.getGender() );
        hts.setFirstTimeVisit( htsDto.getFirstTimeVisit() );
        hts.setMaritalStatus( htsDto.getMaritalStatus() );
        hts.setNumChildren( htsDto.getNumChildren() );
        hts.setNumWives( htsDto.getNumWives() );
        hts.setTypeCounseling( htsDto.getTypeCounseling() );
        hts.setIndexClient( htsDto.getIndexClient() );
        hts.setTypeIndex( htsDto.getTypeIndex() );
        hts.setIndexClientCode( htsDto.getIndexClientCode() );
        hts.setKnowledgeAssessment1( htsDto.getKnowledgeAssessment1() );
        hts.setKnowledgeAssessment2( htsDto.getKnowledgeAssessment2() );
        hts.setKnowledgeAssessment3( htsDto.getKnowledgeAssessment3() );
        hts.setKnowledgeAssessment4( htsDto.getKnowledgeAssessment4() );
        hts.setKnowledgeAssessment5( htsDto.getKnowledgeAssessment5() );
        hts.setKnowledgeAssessment6( htsDto.getKnowledgeAssessment6() );
        hts.setKnowledgeAssessment7( htsDto.getKnowledgeAssessment7() );
        hts.setRiskAssessment1( htsDto.getRiskAssessment1() );
        hts.setRiskAssessment2( htsDto.getRiskAssessment2() );
        hts.setRiskAssessment3( htsDto.getRiskAssessment3() );
        hts.setRiskAssessment4( htsDto.getRiskAssessment4() );
        hts.setRiskAssessment5( htsDto.getRiskAssessment5() );
        hts.setRiskAssessment6( htsDto.getRiskAssessment6() );
        hts.setTbScreening1( htsDto.getTbScreening1() );
        hts.setTbScreening2( htsDto.getTbScreening2() );
        hts.setTbScreening3( htsDto.getTbScreening3() );
        hts.setTbScreening4( htsDto.getTbScreening4() );
        hts.setStiScreening1( htsDto.getStiScreening1() );
        hts.setStiScreening2( htsDto.getStiScreening2() );
        hts.setStiScreening3( htsDto.getStiScreening3() );
        hts.setStiScreening4( htsDto.getStiScreening4() );
        hts.setStiScreening5( htsDto.getStiScreening5() );
        hts.setHivTestResult( htsDto.getHivTestResult() );
        hts.setTestedHiv( htsDto.getTestedHiv() );
        hts.setPostTest1( htsDto.getPostTest1() );
        hts.setPostTest2( htsDto.getPostTest2() );
        hts.setPostTest3( htsDto.getPostTest3() );
        hts.setPostTest4( htsDto.getPostTest4() );
        hts.setPostTest5( htsDto.getPostTest5() );
        hts.setPostTest6( htsDto.getPostTest6() );
        hts.setPostTest7( htsDto.getPostTest7() );
        hts.setPostTest8( htsDto.getPostTest8() );
        hts.setPostTest9( htsDto.getPostTest9() );
        hts.setPostTest10( htsDto.getPostTest10() );
        hts.setPostTest11( htsDto.getPostTest11() );
        hts.setPostTest12( htsDto.getPostTest12() );
        hts.setPostTest13( htsDto.getPostTest13() );
        hts.setPostTest14( htsDto.getPostTest14() );
        hts.setSyphilisTestResult( htsDto.getSyphilisTestResult() );
        hts.setHepatitisbTestResult( htsDto.getHepatitisbTestResult() );
        hts.setHepatitiscTestResult( htsDto.getHepatitiscTestResult() );
        hts.setNote( htsDto.getNote() );
        hts.setStiReferred( htsDto.getStiReferred() );
        hts.setTbReferred( htsDto.getTbReferred() );
        hts.setArtReferred( htsDto.getArtReferred() );
        hts.setLongitude( htsDto.getLongitude() );
        hts.setLatitude( htsDto.getLatitude() );

        return hts;
    }

    @Override
    public List<HtsDto> listHtsToDto(List<Hts> hts) {
        if ( hts == null ) {
            return null;
        }

        List<HtsDto> list = new ArrayList<HtsDto>( hts.size() );
        for ( Hts hts1 : hts ) {
            list.add( htsToDto( hts1 ) );
        }

        return list;
    }

    private Long htsFacilityId(Hts hts) {
        if ( hts == null ) {
            return null;
        }
        Facility facility = hts.getFacility();
        if ( facility == null ) {
            return null;
        }
        Long id = facility.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long htsAssessmentAssessmentId(Hts hts) {
        if ( hts == null ) {
            return null;
        }
        Assessment assessment = hts.getAssessment();
        if ( assessment == null ) {
            return null;
        }
        Long assessmentId = assessment.getAssessmentId();
        if ( assessmentId == null ) {
            return null;
        }
        return assessmentId;
    }

    protected Assessment htsDtoToAssessment(HtsDto htsDto) {
        if ( htsDto == null ) {
            return null;
        }

        Assessment assessment = new Assessment();

        assessment.setAssessmentId( htsDto.getAssessmentId() );

        return assessment;
    }

    protected Facility htsDtoToFacility(HtsDto htsDto) {
        if ( htsDto == null ) {
            return null;
        }

        Facility facility = new Facility();

        facility.setId( htsDto.getFacilityId() );

        return facility;
    }
}
