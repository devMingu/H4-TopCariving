import { Flex, Text } from '@components/common';
import {
  OptionModal,
  alertContentInterface,
} from '@components/makeMyCar/option/OptionModal';
import { optionKey, useMyCar } from '@contexts/MyCarContext';
import { OptionCard, OptionInfoCard } from '@components/makeMyCar/option';

import React, { Suspense, useEffect, useState } from 'react';
import styled from '@emotion/styled';
import { theme } from '@styles/theme';
import { css } from '@emotion/react';
import vector478 from '@assets/images/Vector 478.svg';
import { useLoaderData } from 'react-router-dom';
import { OptionLoader } from '@components/common/Loading/OptionLoader';
import { ArchivePopup } from '@components/makeMyCar';

const defaultCategoryList = [
  '파워트레인/성능',
  '지능형 안전기술',
  '안전',
  '외관',
  '내장',
  '시트',
  '편의',
  '멀티미디어',
];

const cateName = {
  select: '선택항목',
  default: '기본 포함 품목',
};

const pageKey = {
  '/my-car/option': 'selected',
  '/my-car/option/genuine': 'genuine',
  '/my-car/option/performance': 'performance',
} as { [key in string]: optionKey };

export const MyCarOptions = () => {
  const { selectOptionData, defaultOptionData } =
    useLoaderData() as optionDatasInterface;

  const [optionKey, setOptionKey] = useState<optionKey>('selected');

  useEffect(() => {
    setOptionKey(pageKey[window.location.pathname]);
    const handleLocationChange = () => {
      setOptionKey(pageKey[window.location.pathname]);
    };

    window.addEventListener('popstate', handleLocationChange);

    return () => {
      window.removeEventListener('popstate', handleLocationChange);
    };
  }, []);

  const { myCarInfo, setMyCarInfo } = useMyCar();
  const [optionInfo, setOptionInfo] = useState(selectOptionData);

  const [selectedItem, setSelectedItem] = useState(0);
  const [selectedMenu, setSelectedMenu] = useState(cateName.select);
  const [defaultOption, setDefaultOption] = useState(0);
  const [infoImage, setInfoImage] = useState<string>(
    selectOptionData[0].photoUrl,
  );

  const [isOpen, setIsOpen] = useState(false);

  const [currentDefaultCategory, setCurrentDefaultCategory] =
    useState<string>('파워트레인/성능');

  const [modalData, setModalData] = useState<alertContentInterface>({
    title: '',
    imgSrc: '',
    desc: '',
  });

  const getSelectedState = (optionIdx: number) => {
    for (const selectedOption of myCarInfo.option[optionKey]) {
      if (selectedOption.id === optionInfo[optionIdx].carOptionId) {
        return true;
      }
    }
    return false;
  };

  const changeUserOptionList = (optionIdx: number) => {
    if (optionKey === 'performance') {
      // 하나만 선택 가능
      if (
        optionInfo[optionIdx].carOptionId === myCarInfo.option[optionKey][0]?.id
      ) {
        // 삭제
        setMyCarInfo({
          ...myCarInfo,
          price: myCarInfo.price - optionInfo[optionIdx].price,
          option: {
            ...myCarInfo.option,
            [optionKey]: [],
          },
        });
      } else {
        // 추가
        const curPrice = myCarInfo.option[optionKey][0]
          ? myCarInfo.option[optionKey][0].price
          : 0;
        setMyCarInfo({
          ...myCarInfo,
          price: myCarInfo.price - curPrice + optionInfo[optionIdx].price,
          option: {
            ...myCarInfo.option,
            [optionKey]: [
              {
                id: optionInfo[optionIdx].carOptionId,
                name: optionInfo[optionIdx].optionName,
                price: optionInfo[optionIdx].price,
              },
            ],
          },
        });
      }
    } else {
      // 여러 개 선택 가능
      getSelectedState(optionIdx)
        ? setMyCarInfo({
            ...myCarInfo,
            price: myCarInfo.price - optionInfo[optionIdx].price,
            option: {
              ...myCarInfo.option,
              [optionKey]: myCarInfo.option[optionKey].filter(
                (item) => item.id !== optionInfo[optionIdx].carOptionId,
              ),
            },
          })
        : setMyCarInfo({
            ...myCarInfo,
            price: myCarInfo.price + optionInfo[optionIdx].price,
            option: {
              ...myCarInfo.option,
              [optionKey]: [
                ...myCarInfo.option[optionKey],
                {
                  id: optionInfo[optionIdx].carOptionId,
                  name: optionInfo[optionIdx].optionName,
                  price: optionInfo[optionIdx].price,
                },
              ],
            },
          });
    }
  };

  const setSubInfoImage = (imgSrc: string) => {
    setInfoImage(imgSrc);
  };

  const onSelectedItemHandler = (idx: number) => {
    if (selectedMenu === cateName.select) {
      selectedMenu === cateName.select && setSelectedItem(idx);
    } else {
      onModalHandler(idx); // 기본 포함 품목일때 모달창 띄우기
    }
  };

  const onModalHandler = (idx: number) => {
    const defaultOption = defaultOptionData[currentDefaultCategory][idx];
    const modalOptionData: alertContentInterface = {
      title: defaultOption.optionName,
      imgSrc: defaultOption.photoUrl,
      desc: defaultOption?.optionDetail
        ? defaultOption.optionDetail
        : '상세설명이 없습니다.',
    };

    setIsOpen(true);
    setModalData({ ...modalOptionData });
  };

  const modalCloseHandler = () => {
    setIsOpen(false);
  };

  return (
    <Flex
      css={css`
        position: relative;
      `}
    >
      <Flex direction="column" height={623} gap={15}>
        {/* 옵션 상단 */}
        <Flex gap={39} height={320}>
          {/* 이미지 */}
          <Flex width={479}>
            {infoImage && <ImgContainer src={infoImage} alt="" />}
          </Flex>
          {/* 옵션 Info */}
          <Flex direction="column" justify="flex-start">
            {/* 옵션 이름 / 가격 */}
            <OptionContainer>
              <OptionName typo="Heading1_Bold">
                {optionInfo[selectedItem].optionName}
              </OptionName>
              <Text typo="Heading2_Bold">
                +{optionInfo[selectedItem].price.toLocaleString()} 원
              </Text>
            </OptionContainer>
            {/* 옵션에대한 태그칩 */}
            <Flex
              width={507}
              height={30}
              margin="12px 0 18px 0"
              gap={12}
              direction="column"
              justify="flex-start"
            >
              <Flex justify="flex-start" gap={4} height="auto">
                <BottomOptionName>
                  {optionInfo[selectedItem].optionName}
                </BottomOptionName>
                <Text typo="Body3_Regular">
                  에 대해 시승자들은 이런 후기를 남겼어요
                </Text>
              </Flex>
            </Flex>
            {/* 옵션 세부 설명 */}
            <Suspense fallback={<OptionLoader />}>
              <OptionInfoCard
                optionId={optionInfo[selectedItem].carOptionId}
                setSubInfoImage={(imgSrc) => setSubInfoImage(imgSrc)}
              />
            </Suspense>
          </Flex>
        </Flex>

        {/* 옵션 하단 */}
        <Flex direction="column" height={255} gap={20}>
          {/* 선택 항목 / 기본 포함 항목 */}
          <OptionMenu justify="flex-start" height={40} gap={23}>
            <MenuName
              isSelected={selectedMenu === cateName.select}
              onClick={() => {
                if (selectedMenu !== cateName.select) {
                  setOptionInfo(optionInfo);
                  setSelectedMenu(cateName.select);
                }
              }}
            >
              선택항목
            </MenuName>
            <MenuName
              isSelected={selectedMenu === cateName.default}
              onClick={() => {
                if (selectedMenu !== cateName.default) {
                  setSelectedMenu(cateName.default);
                }
              }}
            >
              기본 포함 품목
            </MenuName>
          </OptionMenu>
          {/* 기본 포함 품목 카테고리 */}
          {selectedMenu !== cateName.select && (
            <Flex justify="flex-start" gap={10} height={10}>
              {defaultCategoryList.map((it, idx) => (
                <React.Fragment key={`optionTag_${idx}`}>
                  <OptionTag
                    typo="Body3_Medium"
                    palette={defaultOption === idx ? 'Black' : 'MediumGray'}
                    onClick={() => {
                      setCurrentDefaultCategory(defaultCategoryList[idx]);
                      setDefaultOption(idx);
                    }}
                  >
                    {it}
                  </OptionTag>
                  {idx !== 7 && <img src={vector478} alt="" />}
                </React.Fragment>
              ))}
            </Flex>
          )}
          {/* 옵션 카드 */}
          <OptionListContainer
            justify="flex-start"
            align="flex-end"
            gap={6}
            height={300}
          >
            {(selectedMenu === cateName.select
              ? selectOptionData
              : defaultOptionData[currentDefaultCategory]
            ).map((item, idx) => (
              <div
                key={`optionCard_${item.carOptionId}`}
                onClick={() => onSelectedItemHandler(idx)}
              >
                {optionInfo[idx] && (
                  <OptionCard
                    idx={idx}
                    isSelected={
                      myCarInfo.option[optionKey]
                        ? myCarInfo.option[optionKey].some(
                            (item) => item.id === optionInfo[idx].carOptionId,
                          )
                        : false
                    }
                    dimData={
                      optionInfo[idx]?.optionDetail
                        ? optionInfo[idx].optionDetail
                        : '상세설명이 없습니다.'
                    }
                    optionItem={item}
                    selectedMenu={selectedMenu}
                    changeUserOptionList={changeUserOptionList}
                  />
                )}
              </div>
            ))}
          </OptionListContainer>
        </Flex>
        {isOpen && (
          <OptionModal
            content={modalData}
            buttonInfo={[
              {
                text: '확인',
                color: 'Primary',
                onClick: modalCloseHandler,
              },
            ]}
          />
        )}
      </Flex>
      <ArchivePopup desc={`다른 사람의 팰리세이드 옵션 선택이 궁금하다면?`} />
    </Flex>
  );
};

const OptionListContainer = styled(Flex)`
  overflow-y: scroll;

  ::-webkit-scrollbar {
    display: none;
  }
`;

const OptionTag = styled(Text)`
  cursor: pointer;
`;

export const MenuName = styled(Text)<{ isSelected: boolean }>`
  ${({ isSelected }) =>
    isSelected ? theme.typo.Body1_Medium : theme.typo.Heading4_Bold};
  color: ${({ isSelected }) => !isSelected && theme.palette.LightGray};

  cursor: pointer;
  position: relative; /* 부모 요소에 대해 상대 위치 설정 */
  white-space: nowrap;

  &::after {
    content: '';
    position: absolute;
    left: 0;
    bottom: -5px;
    width: 100%;
    height: 3px;
    background-color: ${theme.palette.Black};
    opacity: ${({ isSelected }) => (isSelected ? 1 : 0)};
    transition: opacity 0.3s;
  }

  &:hover {
    color: ${theme.palette.DarkGray};
  }
  transition: ease 0.3s;
`;

export const OptionMenu = styled(Flex)`
  padding: 4px;
  border-bottom: 2px solid ${theme.palette.LightGray};
`;

const OptionContainer = styled(Flex)`
  width: 507px;
  height: 44px;
  justify-content: space-between;
  align-items: flex-start;

  border-bottom: 2px solid #545454;
`;

const OptionName = styled(Text)`
  width: 300px;
  height: auto;
  overflow-x: scroll;
  white-space: nowrap;

  ::-webkit-scrollbar {
    display: none;
  }
`;

const BottomOptionName = styled.span`
  max-width: 250px;

  ${theme.typo.Heading3_Medium}

  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
`;

const ImgContainer = styled.img`
  width: 479px;
  height: 304px;
  border-radius: 4px;
  object-fit: cover;
`;

interface optionDatasInterface {
  selectOptionData: selectOptionInterface[];
  defaultOptionData: defaultOptionInterface;
  // detailInfoData: optionInfoInterface;
}

export interface urlPathInterface {
  hash: string;
  key: string;
  pathname: string;
  search: string;
  state: string | null;
}

export interface selectOptionInterface {
  carOptionId: number;
  optionName: string;
  optionDetail: string;
  price: number;
  photoUrl: string;
}

export interface optionDetailInterface {
  carOptionId: number;
  optionName: string;
  optionDetail: string;
  photoUrl: string;
}

export interface optionTagInterface {
  tagContent: string;
}

export interface optionItemInterface {
  carOptionId: number;
  optionName: string;
  optionDetail?: string;
  price: number;
  photoUrl: string;
}

export interface defaultOptionInterface {
  [key: string]: optionItemInterface[];
}

export interface detailsOptioinInterface {
  carOptionId: number;
  optionName: string;
  price: number;
  photoUrl: string;
  details: string[];
  tags: tagInterface[];
}

export interface tagInterface {
  tagContent: string;
}
