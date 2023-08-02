import styled from '@emotion/styled';
import { KeyOfPalette } from '../../styles/theme';
import { theme } from '../../styles/theme';

export const Flex = styled.div<{
  direction?: string;
  justify?: string;
  align?: string;
  margin?: string;
  padding?: string;
  gap?: string;
  width?: number;
  height?: number;
  backgroundColor?: KeyOfPalette;
}>`
  display: flex;
  flex-direction: ${({ direction }) => (direction ? `${direction}` : 'row')};
  justify-content: ${({ justify }) => (justify ? `${justify}` : 'center')};
  align-items: ${({ align }) => (align ? `${align}` : 'center')};
  gap: ${({ gap }) => (gap ? `${gap}px` : '0px')};
  width: ${({ width }) => (width ? `${width}px` : '100%')};
  height: ${({ height }) => (height ? `${height}px` : '100%')};
  margin: ${({ margin }) => (margin ? margin : '0')};
  padding: ${({ padding }) => (padding ? padding : '0')};
  box-sizing: border-box;
  background-color: ${({ backgroundColor }) =>
    backgroundColor ? theme.palette[backgroundColor] : `transparent`};
`;
