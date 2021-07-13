import { PALETTE } from 'constants/palette';
import styled from 'styled-components';
import IconButton from '../IconButton/IconButton';

const ModalContainer = styled.div`
  position: fixed;
  display: flex;
  justify-content: center;
  align-items: center;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background-color: rgba(0, 0, 0, 0.3);
`;

const ModalInner = styled.div`
  position: relative;
  display: inline-block;
  background-color: ${PALETTE.WHITE_400};
  border-radius: 25px;
  padding: 60px 40px;
`;

const CrossMarkButton = styled(IconButton)`
  position: absolute;
  top: -8px;
  right: -8px;
  width: 30px;
  height: 30px;
  background-color: #ffe6ca;
  padding: 5px;
`;

export default { ModalContainer, ModalInner, CrossMarkButton };
