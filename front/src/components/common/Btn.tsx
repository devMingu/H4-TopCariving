import styled from '@emotion/styled';
import { KeyOfPalette, theme } from '@styles/theme';

export const Btn = styled.button<{
  width?: number;
  heightType?: string;
  backgroundColor: KeyOfPalette;
  padding?: string;
  border?: number;
}>`
  display: flex;
  justify-content: center;
  align-items: center;
  width: ${({ width }) => (width ? `${width}px` : 'auto')};
  height: ${({ heightType }) =>
    heightType === 'large'
      ? '56px'
      : heightType === 'medium'
      ? '50px'
      : '30px'};

  background-color: ${({ backgroundColor }) => theme.palette[backgroundColor]};

  padding: ${({ padding }) => (padding ? padding : '0')};

  border-radius: ${({ border }) => (border ? `${border}px` : '8px')};
  border: none;
`;