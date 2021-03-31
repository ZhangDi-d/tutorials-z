package org.mapstruct.example.mapper;

import javax.annotation.processing.Generated;
import org.mapstruct.example.dto.FishDto;
import org.mapstruct.example.dto.FishTankWithNestedDocumentDto;
import org.mapstruct.example.dto.WaterQualityOrganisationDto;
import org.mapstruct.example.dto.WaterQualityReportDto;
import org.mapstruct.example.dto.WaterQualityWithDocumentDto;
import org.mapstruct.example.model.Fish;
import org.mapstruct.example.model.FishTank;
import org.mapstruct.example.model.WaterQuality;
import org.mapstruct.example.model.WaterQualityReport;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-03-17T10:36:45+0800",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 14.0.2 (Oracle Corporation)"
)
public class FishTankMapperWithDocumentImpl implements FishTankMapperWithDocument {

    @Override
    public FishTankWithNestedDocumentDto map(FishTank source) {
        if ( source == null ) {
            return null;
        }

        FishTankWithNestedDocumentDto fishTankWithNestedDocumentDto = new FishTankWithNestedDocumentDto();

        fishTankWithNestedDocumentDto.setFish( fishToFishDto( source.getFish() ) );
        fishTankWithNestedDocumentDto.setQuality( waterQualityToWaterQualityWithDocumentDto( source.getQuality() ) );
        fishTankWithNestedDocumentDto.setName( source.getName() );

        return fishTankWithNestedDocumentDto;
    }

    protected FishDto fishToFishDto(Fish fish) {
        if ( fish == null ) {
            return null;
        }

        FishDto fishDto = new FishDto();

        fishDto.setKind( fish.getType() );

        fishDto.setName( "Jaws" );

        return fishDto;
    }

    protected WaterQualityOrganisationDto waterQualityReportToWaterQualityOrganisationDto(WaterQualityReport waterQualityReport) {
        if ( waterQualityReport == null ) {
            return null;
        }

        WaterQualityOrganisationDto waterQualityOrganisationDto = new WaterQualityOrganisationDto();

        waterQualityOrganisationDto.setName( "NoIdeaInc" );

        return waterQualityOrganisationDto;
    }

    protected WaterQualityReportDto waterQualityReportToWaterQualityReportDto(WaterQualityReport waterQualityReport) {
        if ( waterQualityReport == null ) {
            return null;
        }

        WaterQualityReportDto waterQualityReportDto = new WaterQualityReportDto();

        waterQualityReportDto.setVerdict( waterQualityReport.getVerdict() );
        waterQualityReportDto.setOrganisation( waterQualityReportToWaterQualityOrganisationDto( waterQualityReport ) );

        return waterQualityReportDto;
    }

    protected WaterQualityWithDocumentDto waterQualityToWaterQualityWithDocumentDto(WaterQuality waterQuality) {
        if ( waterQuality == null ) {
            return null;
        }

        WaterQualityWithDocumentDto waterQualityWithDocumentDto = new WaterQualityWithDocumentDto();

        waterQualityWithDocumentDto.setDocument( waterQualityReportToWaterQualityReportDto( waterQuality.getReport() ) );

        return waterQualityWithDocumentDto;
    }
}
