import { Flex, Text } from '@components/common';
import vector484 from '@assets/images/Vector 484.svg';
import styled from '@emotion/styled';
import { theme } from '@styles/theme';
import { ArchiveDetailPageProps } from '@pages/Archive/detail';
import { getDate } from '@utils/getDate';
import { CarOptionPosition } from '@components/archive/detail/CarOptionPosition';
import { colorPath } from '@pages/MyCar/Color';
export const ArchiveReview = ({
  detailInfo,
  optionDetail,
}: ArchiveDetailPageProps) => {
  return (
    <Flex direction="column" height={200} backgroundColor="LightSand">
      <CarInfo
        direction="column"
        justify="flex-start"
        width={1040}
        height={334}
        gap={16}
        padding="29px 0 0 0"
      >
        <Flex direction="column" align="flex-start" height="auto">
          <Flex justify="flex-start" gap={25}>
            <Text typo="Heading1_Bold">{`팰리세이드 ${
              optionDetail?.모델[0].optionName &&
              optionDetail?.모델[0].optionName
            }`}</Text>
            <InfoTag>
              <Text typo="Body4_Medium" palette="Gold">
                {`${getDate(
                  new Date(detailInfo?.dayTime ? detailInfo.dayTime : ''),
                )}에 ${
                  detailInfo?.archivingType && detailInfo?.archivingType
                }했어요`}
              </Text>
            </InfoTag>
          </Flex>
          <Text typo="Body1_Regular">{`${
            optionDetail?.엔진 ? optionDetail?.엔진[0].optionName : '선택 없음'
          } / ${
            optionDetail?.구동방식
              ? optionDetail?.구동방식[0].optionName
              : '선택 없음'
          } / ${
            optionDetail?.바디타입
              ? optionDetail?.바디타입[0].optionName
              : '선택 없음'
          }`}</Text>
        </Flex>
        <Flex justify="flex-start" gap={16} height={22}>
          <Flex gap={12} width="auto">
            <Text typo="Body3_Medium">외장</Text>
            <Text typo="Body3_Regular" palette="DarkGray">
              {optionDetail?.외장색상
                ? optionDetail?.외장색상[0].optionName
                : '선택 없음'}
            </Text>
          </Flex>
          <img src={vector484} alt="" />
          <Flex gap={12} width="auto">
            <Text typo="Body3_Medium">내장</Text>
            <Text typo="Body3_Regular" palette="DarkGray">
              {optionDetail?.내장색상
                ? optionDetail?.내장색상[0].optionName
                : '선택 없음'}
            </Text>
          </Flex>
        </Flex>
        <CarContainer>
          <CarOptionPosition
            position={detailInfo?.positions}
            color={
              optionDetail?.외장색상
                ? colorPath[optionDetail?.외장색상[0].optionName]
                : 'abyss'
            }
          />
        </CarContainer>
      </CarInfo>
    </Flex>
  );
};

const CarInfo = styled(Flex)`
  position: relative;
  padding-bottom: 22px;
`;

const InfoTag = styled(Flex)`
  width: auto;
  height: 22px;
  padding: 12px;

  border-radius: 8px;
  background: ${theme.palette.Sand};

  white-space: nowrap;
`;

const CarContainer = styled.div`
  z-index: 2;
  position: absolute;
  right: -200px;
  top: -80px;

  @keyframes movingCar {
    0% {
      opacity: 0;
    }
    100% {
      opacity: 1;
    }
  }
  object-fit: cover;
`;
