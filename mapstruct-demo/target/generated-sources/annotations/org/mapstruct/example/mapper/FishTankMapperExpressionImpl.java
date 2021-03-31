package org.mapstruct.example.mapper;

import javax.annotation.processing.Generated;
import org.mapstruct.example.dto.FishDto;
import org.mapstruct.example.dto.FishTankDto;
import org.mapstruct.example.dto.WaterQualityDto;
import org.mapstruct.example.dto.WaterQualityOrganisationDto;
import org.mapstruct.example.dto.WaterQualityReportDto;
import org.mapstruct.example.model.Fish;
import org.mapstruct.example.model.FishTank;
import org.mapstruct.example.model.WaterQuality;
import org.mapstruct.example.model.WaterQualityReport;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-03-17T10:36:45+0800",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 14.0.2 (Oracle Corporation)"
)
public class FishTankMapperExpressionImpl implements FishTankMapperExpression {

    @Override
    public FishTankDto map(FishTank source) {
        if ( source == null ) {
            return null;
        }

        FishTankDto fishTankDto = new FishTankDto();

        fishTankDto.setFish( fishToFishDto( source.getFish() ) );
        fishTankDto.setName( source.getName() );
        fishTankDto.setQuality( waterQualityToWaterQualityDto( source.getQuality() ) );

        return fishTankDto;
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

        waterQualityOrganisationDto.setName( "Dunno" );

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

    protected WaterQualityDto waterQualityToWaterQualityDto(WaterQuality waterQuality) {
        if ( waterQuality == null ) {
            return null;
        }

        WaterQualityDto waterQualityDto = new WaterQualityDto();

        waterQualityDto.setReport( waterQualityReportToWaterQualityReportDto( waterQuality.getReport() ) );

        return waterQualityDto;
    }
}
