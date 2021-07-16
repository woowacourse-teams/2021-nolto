import styled from 'styled-components';

import { PALETTE } from 'constants/palette';
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
  padding: 6.25rem 4.75rem;
  box-shadow: 4px 4px 4px 4px rgba(0, 0, 0, 0.2);
`;

const CrossMarkButton = styled(IconButton)`
  position: absolute;
  top: -8px;
  right: -8px;
  width: 2rem;
  height: 2rem;
  background-color: ${PALETTE.PRIMARY_400};
  padding: 5px;
`;

export default { ModalContainer, ModalInner, CrossMarkButton };
