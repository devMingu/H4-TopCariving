import { Flex, Text } from '@components/common';
import { useEffect, useState } from 'react';
import { ImgTag, InfoBox } from './Engine';
import { myCarOptionInterface } from '@interface/index';
import { BodyCard } from '@components/makeMyCar/trim';
import { useMyCar } from '@contexts/MyCarContext';
import { useLoaderData } from 'react-router-dom';
import { css } from '@emotion/react';
import { ArchivePopup } from '@components/makeMyCar';

const Traction = () => {
  const tractionInfo = useLoaderData() as myCarOptionInterface[];
  const { myCarInfo, setMyCarInfo } = useMyCar();
  const [isSelected, setIsSelected] = useState(0);

  useEffect(() => {
    const getData = async () => {
      if (tractionInfo) {
        if (!myCarInfo.trim.traction) {
          setMyCarInfo({
            ...myCarInfo,
            price: myCarInfo.price + tractionInfo[0].price,
            trim: {
              ...myCarInfo.trim,
              traction: {
                id: tractionInfo[0].carOptionId,
                name: tractionInfo[0].optionName,
                price: tractionInfo[0].price,
              },
            },
          });
        } else {
          tractionInfo.forEach((traction, selectIdx) => {
            if (traction.carOptionId === myCarInfo.trim.traction?.id) {
              setIsSelected(selectIdx);
            }
          });
        }
      }
    };

    getData();
  }, []);

  const onSelectBodyType = (idx: number) => {
    setMyCarInfo({
      ...myCarInfo,
      trim: {
        ...myCarInfo.trim,
        traction: {
          id: tractionInfo[idx].carOptionId,
          name: tractionInfo[idx].optionName,
          price: tractionInfo[idx].price,
        },
      },
      price:
        myCarInfo.price -
        tractionInfo[isSelected].price +
        tractionInfo[idx].price,
    });
    setIsSelected(idx);
  };

  return (
    <Flex
      padding="28px 0 0 0"
      align="flex-start"
      gap={28}
      css={css`
        position: relative;
      `}
    >
      <Flex direction="column" height="auto" gap={23}>
        <Flex width={620} align="flex-start">
          <ImgTag src={tractionInfo[isSelected].photoUrl} alt="" />
        </Flex>
        <Flex width={620} direction="column" justify="space-between">
          <InfoBox justify="space-between" align="flex-start" height={48}>
            <Text typo="Heading1_Bold">
              {tractionInfo[isSelected].optionName}
            </Text>
            <Text typo="Heading2_Bold">
              +{tractionInfo[isSelected].price.toLocaleString()}원
            </Text>
          </InfoBox>
        </Flex>
      </Flex>

      <Flex direction="column" justify="flex-start" gap={12}>
        {tractionInfo.map((body, idx) => (
          <div
            key={`engineOption_${idx}`}
            onClick={() => {
              onSelectBodyType(idx);
            }}
          >
            <BodyCard option={body} isSelected={isSelected === idx} />
          </div>
        ))}
      </Flex>
      <ArchivePopup
        desc={`${tractionInfo[isSelected].optionName}의 리얼한 후기가 궁금하다면?`}
      />
    </Flex>
  );
};

export default Traction;
