import { ArchiveReview, ArchiveShortInfo } from '@components/archive/detail';
import { ArchiveOptionDetails } from '@components/archive/detail/ArchiveOptionDetails';
import { positionInterface } from '@components/archive/detail/CarOptionPosition';
import styled from '@emotion/styled';
import { ArchiveUrl, apiInstance } from '@utils/api';
import { getOptionKeyInfo } from '@utils/getCarInfo';
import { useEffect, useState } from 'react';

export const ArchiveDetail = () => {
  const [detailInfo, setDetailInfo] = useState<archiveDetailInterface>();
  const [optionDetail, setOptionDetail] = useState<{
    [key in string]: archiveOptionDetailInterface[];
  }>();

  const getData = async () => {
    const urlSearchParams = new URL(location.href).searchParams;
    const archivingId = urlSearchParams.get('id');

    const data = (await apiInstance({
      url: `${ArchiveUrl.DETAIL}/${archivingId}`,
      method: 'GET',
    })) as archiveDetailInterface;

    setDetailInfo(data);
    setOptionDetail(getOptionKeyInfo(data.optionDetails));
  };

  useEffect(() => {
    getData();
  }, []);

  return (
    <>
      <TopMargin />
      <ArchiveReview detailInfo={detailInfo} optionDetail={optionDetail} />
      <ArchiveShortInfo
        detailInfo={detailInfo}
        optionDetail={optionDetail}
        getData={getData}
      />
      <ArchiveOptionDetails
        detailInfo={detailInfo}
        optionDetail={optionDetail}
      />
    </>
  );
};

const TopMargin = styled.div`
  height: 151px;
  flex-shrink: 0;
`;

export interface ArchiveDetailPageProps {
  detailInfo?: archiveDetailInterface;
  optionDetail?: {
    [key in string]: archiveOptionDetailInterface[];
  };
}

export interface archiveOptionDetailInterface {
  carOptionId: number;
  optionName: string;
  categoryDetail: string;
  photoUrl: string;
  childOptionNames: string[];
  positionId: null;
  price: number;
  tags: { tagContent: string }[];
}

export interface archiveDetailInterface {
  archivingId: number;
  dayTime: string;
  archivingType: string;
  totalPrice: number;
  positions: positionInterface[];
  isBookmarked: boolean;
  optionDetails: archiveOptionDetailInterface[];
  carReview: '너무 좋아요';
  tags: { tagContent: string }[];
}
