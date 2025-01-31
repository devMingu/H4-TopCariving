import styled from '@emotion/styled';
import { Flex, Text } from '@components/common';
import { useEffect, useState } from 'react';
import { EngineCard } from '@components/makeMyCar/trim';
import { MyCarContextType, useMyCar } from '@contexts/MyCarContext';
import { useLoaderData } from 'react-router-dom';
import { ArchivePopup } from '@components/makeMyCar';
import { css } from '@emotion/react';

export interface engineInfoInterface {
  carOptionId: number;
  optionName: string;
  optionDetail: string;
  price: number;
  maxOutput: string;
  maxTorque: string;
  photoUrl: string;
}

const Engine = () => {
  const engineInfo = useLoaderData() as engineInfoInterface[];
  const { myCarInfo, setMyCarInfo } = useMyCar() as MyCarContextType;
  const [isSelected, setIsSelected] = useState(0);

  useEffect(() => {
    const getData = async () => {
      if (engineInfo) {
        if (!myCarInfo.trim.engine) {
          setMyCarInfo({
            ...myCarInfo,
            price: myCarInfo.price + engineInfo[0].price,
            trim: {
              ...myCarInfo.trim,
              engine: {
                id: engineInfo[0].carOptionId,
                name: engineInfo[0].optionName,
                price: engineInfo[0].price,
              },
            },
          });
        } else {
          engineInfo.forEach((engine, selectIdx) => {
            if (engine.carOptionId === myCarInfo.trim.engine?.id) {
              setIsSelected(selectIdx);
            }
          });
        }
      }
    };

    getData();
  }, []);

  const onSelectEngine = (idx: number) => {
    engineInfo &&
      setMyCarInfo({
        ...myCarInfo,
        price:
          myCarInfo.price -
          engineInfo[isSelected].price +
          engineInfo[idx].price,
        trim: {
          ...myCarInfo.trim,
          engine: {
            id: engineInfo[idx].carOptionId,
            name: engineInfo[idx].optionName,
            price: engineInfo[idx].price,
          },
        },
      });
    setIsSelected(idx);
  };

  return (
    <>
      {engineInfo && (
        <Flex
          gap={28}
          padding="28px 0 0 0"
          align="flex-start"
          css={css`
            position: relative;
          `}
        >
          <Flex direction="column" gap={23} height="auto">
            <Flex width={620} align="flex-start">
              <ImgTag src={engineInfo[isSelected].photoUrl} alt="" />
            </Flex>
            <Flex width={620} direction="column" justify="space-between">
              <InfoBox justify="space-between" align="flex-start" height={48}>
                <Text typo="Heading1_Bold">
                  {engineInfo[isSelected].optionName}
                </Text>
                <Text typo="Heading2_Bold">
                  +{engineInfo[isSelected].price.toLocaleString()}원
                </Text>
              </InfoBox>
            </Flex>
          </Flex>

          <Flex direction="column" justify="flex-start" gap={12} height="auto">
            {engineInfo.map((engine, idx) => (
              <div
                key={`engineOption_${idx}`}
                onClick={() => {
                  onSelectEngine(idx);
                }}
              >
                <EngineCard engine={engine} isSelected={isSelected === idx} />
              </div>
            ))}
          </Flex>
          <ArchivePopup
            desc={`${engineInfo[isSelected].optionName}의 리얼한 후기가 궁금하다면?`}
          />
        </Flex>
      )}
    </>
  );
};

export const InfoBox = styled(Flex)`
  border-bottom: 4px solid #545454;
`;

export const ImgTag = styled.img`
  width: 620px;
  height: 397px;
  border-radius: 8px;
  object-fit: cover;
`;

export default Engine;
